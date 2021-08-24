package my.ourShef.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.dto.Acquaintance;
import my.ourShef.controller.dto.AcquaintanceDto;
import my.ourShef.controller.dto.ReceivedRelationshipRequestDto;
import my.ourShef.controller.dto.SendedRelationshipRequestDto;
import my.ourShef.controller.form.AcquaintanceRequestForm;
import my.ourShef.domain.Comment;
import my.ourShef.domain.RelationshipRequest;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.domain.UserAcquaintance;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.domain.constant.RelationshipRequestState;
import my.ourShef.service.CommentService;
import my.ourShef.service.RelationshipRequestService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.UserAcquaintanceService;
import my.ourShef.service.bridge.VisitorVisitedSpotService;

@Controller
@RequestMapping("/acquaintance")
@Slf4j
@RequiredArgsConstructor
public class AcquaintanceContoller {

	private final UserService userService;
	private final SpotService spotService;
	private final CommentService commentService;
	private final CommentController commentController;
	private final VisitorVisitedSpotService visitorVisitedSpotService;
	private final UserAcquaintanceService userAcquaintanceService;
	private final RelationshipRequestService relationshipRequestService;

	@GetMapping("/myAcquaintanceList")
	public String myAcquaintanceList(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			Model model) {

		User loginUser = userService.findByAccountId(LoginUserAccountId).get();

		// bring up a list of acquaintances
		List<User> acquaintanceList = userAcquaintanceService.findAcquaintanceByUser(loginUser);

		// Dto setting
		List<AcquaintanceDto> acquaintanceDtoList = new ArrayList<AcquaintanceDto>();
		for (User acUser : acquaintanceList) {
			// DTO
			AcquaintanceDto acquaintanceDto = new AcquaintanceDto();
			acquaintanceDto.setId(acUser.getId());
			acquaintanceDto.setNickName(acUser.getNickName());
			acquaintanceDto.setProfileImgStoreName(acUser.getProfileImgInfo().getStoreFileName());
			acquaintanceDto.setReliability(acUser.getReliability());

			acquaintanceDtoList.add(acquaintanceDto);
		}
		
		////Check how many unconfirmed Acquaintance requests are there
		long beforeConfirmationRequestNum = relationshipRequestService.getReceivedRelationshipRequest(loginUser, loginUser)
		.stream().filter(e -> (e.getState()==RelationshipRequestState.BEFORE_CONFIRMATION)).count();

		model.addAttribute("beforeConfirmationRequestNum", beforeConfirmationRequestNum);
		model.addAttribute("acquaintanceDtoList", acquaintanceDtoList);

		return "/acquaintance/acquaintanceList";
	}
	
	@Transactional
	@PostMapping("/delete")
	public String deleteAcquaintance(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			@RequestParam("acquaintanceId") List<Long> acquaintanceIds) {
		
		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		
		List<User> loginUserAcquaintanceList = userService.getAcquaintanceList(loginUser);
		
		HashSet<User> loginUserAcquaintanceSet = new HashSet<User>(loginUserAcquaintanceList);
		
		
		for(Long acquaintanceId : acquaintanceIds)
		{
			
			Optional<User> acquaintanceOptional = userService.findById(acquaintanceId);
			
			//If an acquaintance of the ID exists as a user
			if(acquaintanceOptional.isPresent())
			{
				User acquaintance = acquaintanceOptional.get();
				
				//If the user is registered as an acquaintance of the login user
				if(loginUserAcquaintanceSet.contains(acquaintance))
				{
					
					//Delete Comment Entity from spot & Spot user average star rating update
					//The Spot Registrant Reliability Updates
					//Delete VisitorVisited Entity
					/*
					 * 1. Delete all user A's comments from user B's spot.
					 * 1-2. Spot user average star rating update
					 * 1-3. The Spot Registrant Reliability Updates
					 * 2. Delete user A's visitedSpot information for user B's spot.
					 */
					deleteDataOfAFromB(loginUser,acquaintance);
					//does the opposite too.
					deleteDataOfAFromB(acquaintance,loginUser);
					
					//delete UserAcquaintance Entity
					List<UserAcquaintance> userAcquaintanceListByLoginUserAndAcquaintance 
					= userAcquaintanceService.findByUserAndAcquaintance(loginUser, acquaintance);
					List<UserAcquaintance> userAcquaintanceListByAcquaintanceAndLoginUser
					= userAcquaintanceService.findByUserAndAcquaintance(acquaintance, loginUser);
					
					userAcquaintanceService.deletes(userAcquaintanceListByLoginUserAndAcquaintance);
					userAcquaintanceService.deletes(userAcquaintanceListByAcquaintanceAndLoginUser);
										
				}
			}
		}
		
		
		return "redirect:/acquaintance/myAcquaintanceList";
		
	}
	
	/*
	 * 1. Delete all user A's comments from user B's spot.
	 * 1-2. Spot user average star rating update
	 * 1-3. The Spot Registrant Reliability Updates
	 * 2. Delete user A's visitedSpot information for user B's spot.
	 */
	public void deleteDataOfAFromB(User userA, User userB)
	{
		//Delete Comment Entity
		//Delete all user A's comments from user B's spot.
		//and Recalculation of user average star rating
		List<Spot> registeredSpotsByUserB = spotService.getAllRegisteredSpotsByUser(userB);
		for(Spot registeredSpotByUserB : registeredSpotsByUserB)
		{
			List<Comment> comments = registeredSpotByUserB.getComments();
			float oldUsersStarPoint = registeredSpotByUserB.getUsersStarPoint();
			float totalUserSpotPoint = 0;
			float leftCommentsNum = 0;
			boolean isUserACommentExist = false;
			for(Comment comment : comments)
			{
				if(comment.getCommentUser()==userA)
				{
					//delete Commnet Entity
					commentService.delete(comment);
					
					if(isUserACommentExist==false)
						isUserACommentExist=true;
				}
				else
				{
					totalUserSpotPoint = totalUserSpotPoint + comment.getStarPoint();
					leftCommentsNum++;
				}	
			}
			
			if(isUserACommentExist==true)
			{
				//Update user average star rating
				if(leftCommentsNum == 0) {
					registeredSpotByUserB.setUsersStarPoint(-1);
				}
				else
				{
					registeredSpotByUserB.setUsersStarPoint(totalUserSpotPoint/leftCommentsNum);
				}
				
				//Update registrant's reliability
				commentController.updateRegistrantReliability(registeredSpotByUserB);
				
			}
				
			
			//Delete VisitorVisited Entity
			//Delete user A's visitedSpot information for user B's spot.
			Optional<VisitorVisitedSpot> visitorVisitedSpotOptional = visitorVisitedSpotService.findOneByUserAndSpot(userA, registeredSpotByUserB);
			if(visitorVisitedSpotOptional.isPresent())
			{
				visitorVisitedSpotService.delete(visitorVisitedSpotOptional.get());
			}
			
		}
		
	}
	
	


	@GetMapping("/relationshipRequestBox")
	public String getRelationshipRequestBox(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			Model model) {

		User loginUser = userService.findByAccountId(LoginUserAccountId).get();

		List<RelationshipRequest> sendedRelationshipRequestList = relationshipRequestService
				.getSendedRelationshipRequest(loginUser, loginUser);
		List<SendedRelationshipRequestDto> sendedRelationshipRequestDtoList = new ArrayList<SendedRelationshipRequestDto>();

		for (RelationshipRequest sendedRelationshipRequest : sendedRelationshipRequestList) {
			SendedRelationshipRequestDto sendedRelationshipRequestDto = new SendedRelationshipRequestDto();
			sendedRelationshipRequestDto.setId(sendedRelationshipRequest.getId());
			sendedRelationshipRequestDto.setState(sendedRelationshipRequest.getState());
			sendedRelationshipRequestDto.setToUserNickName(sendedRelationshipRequest.getToUser().getNickName());

			sendedRelationshipRequestDtoList.add(sendedRelationshipRequestDto);

		}
		
		//sort by enum ordinal
		//Requests before confirmation should be at the top.
		Collections.sort(sendedRelationshipRequestDtoList);

		model.addAttribute("sendedRelationshipRequestDtoList", sendedRelationshipRequestDtoList);

		return "acquaintance/relationshipRequestBox";
	}

	@GetMapping("/relationshipResponseBox")
	public String getRelationshipResponseBox(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			Model model) {

		User loginUser = userService.findByAccountId(LoginUserAccountId).get();

		List<RelationshipRequest> receivedRelationshipRequestList = relationshipRequestService.getReceivedRelationshipRequest(loginUser, loginUser);
				
		
		List<ReceivedRelationshipRequestDto> receivedRelationshipRequestDtoList = new ArrayList<ReceivedRelationshipRequestDto>();

		for (RelationshipRequest receivedRelationshipRequest : receivedRelationshipRequestList) {
			ReceivedRelationshipRequestDto receivedRelationshipRequestDto = new ReceivedRelationshipRequestDto();
			receivedRelationshipRequestDto.setId(receivedRelationshipRequest.getId());
			receivedRelationshipRequestDto.setState(receivedRelationshipRequest.getState());
			receivedRelationshipRequestDto.setFromUserNickName(receivedRelationshipRequest.getFromUser().getNickName());

			receivedRelationshipRequestDtoList.add(receivedRelationshipRequestDto);

		}

		//sort by enum ordinal
		//Requests before confirmation should be at the top.
		Collections.sort(receivedRelationshipRequestDtoList);
		
		model.addAttribute("receivedRelationshipRequestDtoList", receivedRelationshipRequestDtoList);

		return "acquaintance/relationshipResponseBox";
	}

	@GetMapping("/requestForm")
	public String createForm(Model model) {

		AcquaintanceRequestForm acquaintanceRequestForm = new AcquaintanceRequestForm();
		model.addAttribute("acquaintanceRequestForm", acquaintanceRequestForm);

		return "/acquaintance/acquaintanceRequestForm";
	}

	@Transactional
	@PostMapping("/requestForm")
	public String requestRelationship(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			@Valid @ModelAttribute AcquaintanceRequestForm acquaintanceRequestForm, BindingResult bindingResult) {

		User fromUser = userService.findByAccountId(LoginUserAccountId).get();

		// Verifies that the user ID exists
		Optional<User> accquaintanceOptional = userService
				.findByAccountId(acquaintanceRequestForm.getAcquaintanceAccountId());
		if (accquaintanceOptional.isEmpty()) {
			bindingResult.rejectValue("acquaintanceAccountId", "notExist");
		}

		if (bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "/acquaintance/acquaintanceRequestForm";
		}

		User toUser = accquaintanceOptional.get();

		// Check if you are already an acquaintance
		if (userService.getAcquaintanceList(fromUser).stream().filter(ac -> ac.getId() == toUser.getId()).findFirst()
				.isPresent()) {
			bindingResult.rejectValue("acquaintanceAccountId", "already.exist");
		}

		// Check if He or She have been already applied for an acquaintance
		// And if the request is not answered
		// @param1 owner @param2 fromUser @param3 toUser
		List<RelationshipRequest> oldRelationshipRequestListForToUser = relationshipRequestService
				.findByOwnerAndFromUserAndToUser(toUser, fromUser, toUser);
		for (RelationshipRequest oldRelationshipRequestForToUser : oldRelationshipRequestListForToUser) {
			if (oldRelationshipRequestForToUser.getState() == RelationshipRequestState.BEFORE_CONFIRMATION) {
				bindingResult.rejectValue("acquaintanceAccountId", "already.requested");
			}
		}

		if (bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "/acquaintance/acquaintanceRequestForm";
		}

		//// Success Logic

		// If there is a request message left for toUser and fromUser, delete the old
		// message.
		for (RelationshipRequest oldRelationshipRequestForToUser : oldRelationshipRequestListForToUser) {
			relationshipRequestService.delete(oldRelationshipRequestForToUser);
		}
		List<RelationshipRequest> oldRelationshipRequestListForFromUser = relationshipRequestService
				.findByOwnerAndFromUserAndToUser(fromUser, fromUser, toUser);
		for (RelationshipRequest oldRelationshipRequestForFromUser : oldRelationshipRequestListForFromUser) {
			relationshipRequestService.delete(oldRelationshipRequestForFromUser);
		}

		// @param1 fromUser @param2 toUser
		RelationshipRequest myRelationshipRequest = new RelationshipRequest(fromUser, toUser);
		RelationshipRequest opponentRelationshipRequest = new RelationshipRequest(fromUser, toUser);

		myRelationshipRequest.setOwner(fromUser);
		opponentRelationshipRequest.setOwner(toUser);

		// set opponent message ID
		myRelationshipRequest.setOpponentId(opponentRelationshipRequest.getId());
		opponentRelationshipRequest.setOpponentId(myRelationshipRequest.getId());

		// persist
		relationshipRequestService.save(myRelationshipRequest);
		relationshipRequestService.save(opponentRelationshipRequest);

		return "redirect:/acquaintance/relationshipRequestBox";
	}
	
	@Transactional
	@PostMapping("/deleteReceivedRelationshipRequest")
	public String deleteReceivedRelationshipRequest(@RequestParam("relationshipRequestId") List<String> relationshipRequestIds)
	{
		deleteRelationshipRequest(relationshipRequestIds);
		
		return "redirect:/acquaintance/relationshipResponseBox";
	}
	
	@Transactional
	@PostMapping("/deleteSendedRelationshipRequest")
	public String deleteSendedRelationshipRequest(@RequestParam("relationshipRequestId") List<String> relationshipRequestIds)
	{
		deleteRelationshipRequest(relationshipRequestIds);
		return "redirect:/acquaintance/relationshipRequestBox";
	}
	
	
	@Transactional
	@PostMapping("/acceptRelationshipRequest")
	public String acceptRelationshipRequest(@RequestParam("relationshipRequestId") String relationshipRequestId) {
	
	//update RelationshipRequest State
	if(updateRelationshipRequestState(relationshipRequestId, RelationshipRequestState.ACCEPTED))
	{
		RelationshipRequest relationshipRequest = relationshipRequestService.findById(relationshipRequestId).get();
		
		User toUser = relationshipRequest.getToUser();
		User fromUser = relationshipRequest.getFromUser();
		//If acquaintances have not been added to each other
		if(userAcquaintanceService.findAcquaintanceByUser(toUser).stream().filter(ac -> (ac == fromUser)).findFirst().isEmpty()&&
				userAcquaintanceService.findAcquaintanceByUser(fromUser).stream().filter(ac -> (ac == toUser)).findFirst().isEmpty())
		{
			//Perpetuation of acquaintance relationships
			UserAcquaintance uaToUserFromUser = new UserAcquaintance(toUser, fromUser);
			UserAcquaintance uaFromUserToUser = new UserAcquaintance(fromUser, toUser);
			//persist
			userAcquaintanceService.save(uaToUserFromUser);
			userAcquaintanceService.save(uaFromUserToUser);
		}
	}
		
	return "redirect:/acquaintance/relationshipResponseBox";
	}

	
	@Transactional
	@PostMapping("/declineRelationshipRequest")
	public String declineRelationshipRequest(@RequestParam("relationshipRequestId") String relationshipRequestId) {
		
		//update RelationshipRequest State
		updateRelationshipRequestState(relationshipRequestId, RelationshipRequestState.REJECTED);	
		
	return "redirect:/acquaintance/relationshipResponseBox";
	}
	
	
	/*
	 * Update both the sender's and the receiver's status of the request message.
	 * @return true if there is a request message before confirmation, false if not
	 */
	private boolean updateRelationshipRequestState(String relationshipRequestId, RelationshipRequestState state) {
		
		boolean isThereRelationshipRequestBeforeConfirmation = false;
		
		Optional<RelationshipRequest> relationshipRequestOptioanl = relationshipRequestService.findById(relationshipRequestId);
		if(relationshipRequestOptioanl.isPresent()&& relationshipRequestOptioanl.get().getState()==RelationshipRequestState.BEFORE_CONFIRMATION)
		{
			isThereRelationshipRequestBeforeConfirmation = true;
			
			RelationshipRequest relationshipRequest = relationshipRequestOptioanl.get();
			
			relationshipRequest.setState(state);
			
			String opponentRequestId = relationshipRequest.getOpponentId();
			Optional<RelationshipRequest> opponentRelationshipRequestOptional = relationshipRequestService.findById(opponentRequestId);
			if(opponentRelationshipRequestOptional.isPresent())
			{
				RelationshipRequest opponentRelationshipRequest = opponentRelationshipRequestOptional.get();
				opponentRelationshipRequest.setState(state);
			}
			
		}
		
		return isThereRelationshipRequestBeforeConfirmation;
	}
	
	private void deleteRelationshipRequest(List<String> relationshipRequestIds) {
		for(String relationshipRequestId : relationshipRequestIds)
		{
			Optional<RelationshipRequest> relationshipRequestOptional = relationshipRequestService.findById(relationshipRequestId);
			if(relationshipRequestOptional.isPresent())
			{
				RelationshipRequest relationshipRequest = relationshipRequestOptional.get();
				
				/*
				 * If the owner and the receiver are same in the request, 
				 * if the request is deleted before confirmation, 
				 * the status of the opponent relationshipRequest is set to Rejected.
				 */
				if(relationshipRequest.getToUser() == relationshipRequest.getOwner())
				{
					if(relationshipRequest.getState()==RelationshipRequestState.BEFORE_CONFIRMATION)
					{
						
						Optional<RelationshipRequest> opponentRelationshipRequestOptional
						= relationshipRequestService.findById(relationshipRequest.getOpponentId());
						if(opponentRelationshipRequestOptional.isPresent())
						{
							opponentRelationshipRequestOptional.get().setState(RelationshipRequestState.REJECTED);
						}
						
					}
				}
				
				relationshipRequestService.delete(relationshipRequest);
			}
			
		}
	}

	

}
