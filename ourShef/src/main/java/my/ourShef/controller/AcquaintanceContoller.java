package my.ourShef.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.dto.Acquaintance;
import my.ourShef.controller.dto.AcquaintanceDto;
import my.ourShef.controller.dto.SendedRelationRequestDto;
import my.ourShef.controller.form.AcquaintanceRequestForm;
import my.ourShef.domain.RelationshipRequest;
import my.ourShef.domain.User;
import my.ourShef.domain.constant.RelationshipRequestState;
import my.ourShef.service.RelationshipRequestService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.UserAcquaintanceService;


@Controller
@RequestMapping("/acquaintance")
@Slf4j
@RequiredArgsConstructor
public class AcquaintanceContoller {
	
	private final UserService userService;
	private final UserAcquaintanceService userAcquaintanceServie;
	private final RelationshipRequestService relationshipRequestService;

	@GetMapping("/myAcquaintanceList")
	public String myAcquaintanceList(
			@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId, Model model)
	{
		
		 User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		
		//bring up a list of acquaintances
		List<User> acquaintanceList = userAcquaintanceServie.findByUser(loginUser);
		
		//Dto setting
		List<AcquaintanceDto> acquaintanceDtoList = new ArrayList<AcquaintanceDto>();
		for(User acUser : acquaintanceList)
		{
			//DTO
			AcquaintanceDto acquaintanceDto = new AcquaintanceDto();
			acquaintanceDto.setId(acUser.getId());
			acquaintanceDto.setNickName(acUser.getNickName());
			acquaintanceDto.setProfileImgStoreName(acUser.getProfileImgInfo().getStoreFileName());
			acquaintanceDto.setReliability(acUser.getReliability());
			
			acquaintanceDtoList.add(acquaintanceDto);
		}
		
		model.addAttribute("acquaintanceDtoList", acquaintanceDtoList);
		
		return "/acquaintance/acquaintanceList";
	}
	
	
	@GetMapping("/relationshipResponseBox")
	public String getRelationshipResponseBox() {
		
		
		return "acquaintance/relationshipResponseBox";
	}
	
	@GetMapping("/relationshipRequestBox")
	public String getRelationshipRequestBox(@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			Model model) {
		
		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		
		
		List<RelationshipRequest> sendedRelationshipRequestList = relationshipRequestService.getSendedRelationshipRequest(loginUser, loginUser);
		List<SendedRelationRequestDto> sendedRelationRequestDtoList = new ArrayList<SendedRelationRequestDto>();
		
		for(RelationshipRequest sendedRelationshipRequest : sendedRelationshipRequestList)
		{
			SendedRelationRequestDto sendedRelationRequestDto = new SendedRelationRequestDto();
			sendedRelationRequestDto.setId(sendedRelationshipRequest.getId());
			sendedRelationRequestDto.setState(sendedRelationshipRequest.getState());
			sendedRelationRequestDto.setToUserNickName(sendedRelationshipRequest.getToUser().getNickName());
			
			sendedRelationRequestDtoList.add(sendedRelationRequestDto);
			System.out.println("메세지 아이디" + sendedRelationshipRequest.getId());
		}
		
		model.addAttribute("sendedRelationRequestDtoList", sendedRelationRequestDtoList);
		
		return "acquaintance/relationshipRequestBox";
	}
	
	@GetMapping("/requestForm")
	public String createForm(Model model) {
		
		AcquaintanceRequestForm acquaintanceRequestForm = new AcquaintanceRequestForm();
		model.addAttribute("acquaintanceRequestForm", acquaintanceRequestForm);
		
		return "/acquaintance/acquaintanceRequestForm";
	}
	
	@Transactional
	@PostMapping("/requestForm")
	public String requestRelationship(@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			@Valid @ModelAttribute AcquaintanceRequestForm acquaintanceRequestForm, BindingResult bindingResult) {
		
		User fromUser = userService.findByAccountId(LoginUserAccountId).get();
		
		//Verifies that the user ID exists
		Optional<User> accquaintanceOptional= userService.findByAccountId(acquaintanceRequestForm.getAcquaintanceAccountId());
		if(accquaintanceOptional.isEmpty())
		{
			bindingResult.rejectValue("acquaintanceAccountId", "notExist");
		}
		
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "/acquaintance/acquaintanceRequestForm";
		}
			
		
		User toUser = accquaintanceOptional.get();
		
	
		//Check if you are already an acquaintance
		if(userService.getAcquaintanceList(fromUser).stream().filter(ac -> ac.getId() == toUser.getId()).findFirst().isPresent())
		{
			bindingResult.rejectValue("acquaintanceAccountId", "already.exist");
		}
		
		//Check if He or She have been already applied for an acquaintance
		//And if the request is not answered
		//@param1 owner @param2 fromUser @param3 toUser
		List<RelationshipRequest> oldRelationshipRequestListForToUser = 
				relationshipRequestService.findByOwnerAndFromUserAndToUser(toUser, fromUser, toUser);
		for(RelationshipRequest oldRelationshipRequestForToUser : oldRelationshipRequestListForToUser)
		{
			if(oldRelationshipRequestForToUser.getState() == RelationshipRequestState.BEFORE_CONFIRMATION)
			{
				bindingResult.rejectValue("acquaintanceAccountId","already.requested");
			}
		}
		
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "/acquaintance/acquaintanceRequestForm";
		}
		
		////Success Logic
		
		//If there is a request message left for toUser and fromUser, delete the old message.
		for(RelationshipRequest oldRelationshipRequestForToUser : oldRelationshipRequestListForToUser)
		{
			relationshipRequestService.delete(oldRelationshipRequestForToUser);
		}
		List<RelationshipRequest> oldRelationshipRequestListForFromUser= relationshipRequestService.findByOwnerAndFromUserAndToUser(fromUser, fromUser, toUser);
		for(RelationshipRequest oldRelationshipRequestForFromUser : oldRelationshipRequestListForFromUser)
		{
			relationshipRequestService.delete(oldRelationshipRequestForFromUser);
		}
		
		
		//@param1 fromUser @param2 toUser
		RelationshipRequest myRelationshipRequest = new RelationshipRequest(fromUser, toUser);
		RelationshipRequest opponentRelationshipRequest = new RelationshipRequest(fromUser, toUser);
		
		myRelationshipRequest.setOwner(fromUser);
		opponentRelationshipRequest.setOwner(toUser);
		
		//set opponent message ID
		myRelationshipRequest.setOpponentId(opponentRelationshipRequest.getId());
		opponentRelationshipRequest.setOpponentId(myRelationshipRequest.getId());
		
		//persist
		relationshipRequestService.save(myRelationshipRequest);
		relationshipRequestService.save(opponentRelationshipRequest);
		
		
		return "redirect:/acquaintance/relationshipRequestBox";	
	}
	
	
}
