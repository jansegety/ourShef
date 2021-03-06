package my.ourShef.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.form.CommentForm;
import my.ourShef.controller.form.CommentModificationForm;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.service.CommentService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.VisitorVisitedSpotService;

@RequestMapping("/comment")
@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

	private final UserService userService;
	private final SpotService spotService;
	private final CommentService commentService;
	private final VisitorVisitedSpotService visitorVisitedSpotService;
	private final EntityManager em;

	@Transactional
	@PostMapping("/post")
	public String postComment(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			@Valid @ModelAttribute CommentForm commentForm, BindingResult bindingResult) {
		
		//검증에 실패하면 다시 입력 폼으로
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "redirect:/spot/spot/" + commentForm.getSpotId();
		}
		
		//성공 로직

		User commentUser = userService.findByAccountId(LoginUserAccountId).get();
		Spot commentedSpot = spotService.findById(commentForm.getSpotId()).get();

		Comment newComment = new Comment(commentUser, commentedSpot);
		// persist
		commentService.save(newComment);

		newComment.setComment(commentForm.getComment());
		newComment.setStarPoint(commentForm.getStarPoint());

		// Update Average Star Point and Registrant's Reliability
		Long oldVisits = commentedSpot.getVisits();
		float oldUsersStarPoint = commentedSpot.getUsersStarPoint();

		float allStarPoint = 0;
		if (oldUsersStarPoint == -1) {
			allStarPoint = newComment.getStarPoint();
		} else {
			allStarPoint = (oldUsersStarPoint * oldVisits) + newComment.getStarPoint();
		}

		float averageStarPoint = allStarPoint / (oldVisits + 1);

		// update visits
		commentedSpot.setVisits(oldVisits + 1);
		// update usersStarPoint
		commentedSpot.setUsersStarPoint(averageStarPoint); 
		//update reliability of spot
		if(commentedSpot.getUsersStarPoint() != -1)
		{
			float newRelability = 100-Math.abs(commentedSpot.getRegistrantStarPoint() - commentedSpot.getUsersStarPoint());
			commentedSpot.setReliability(newRelability); 
		}

		// Registrant's reliability updates every time a comment is posted
		// Average userStarPoint should be updated.
		updateRegistrantReliability(commentedSpot);

		// update visitorVisitedSpot
		// before update, validation
		Optional<VisitorVisitedSpot> OptionalVisitorVisitedSpot = visitorVisitedSpotService
				.findOneByUserAndSpot(commentUser, commentedSpot);

		// If the spot has been already visited by the user, do not update it.
		if (!OptionalVisitorVisitedSpot.isPresent()) {
			// persist
			visitorVisitedSpotService.save(new VisitorVisitedSpot(commentUser, commentedSpot));
		}

		return "redirect:/spot/spot/" + commentForm.getSpotId();
	}

	@Transactional
	@PostMapping("/delete")
	public String deleteComment(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			@RequestParam("commentId") Long commentId) {	
		
		Comment commentToBeDeleted = commentService.findById(commentId).get();
		
		////permission check
		//Confirm that logingUser is the person who wrote the comment
		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		if(!commentService.getAllCommentListByUser(loginUser).contains(commentToBeDeleted))
		{
			return "error/doNotHavePermission";
		}
		
		
		//Remove from persistence context
		commentService.delete(commentToBeDeleted);
		em.flush();
		
		Spot commentedSpot = commentToBeDeleted.getCommentedSpot();

		// Update Average Star Point and Registrant's Reliability
		Long oldVisits = commentedSpot.getVisits();
		float oldUsersStarPoint = commentedSpot.getUsersStarPoint();

		float allStarPoint = 0;
		float averageStarPoint = 0;
		
		if(oldVisits==1)
		{
			averageStarPoint = -1;
			commentedSpot.setReliability(-1);
			
		}
		else
		{
		allStarPoint = (oldUsersStarPoint * oldVisits) - commentToBeDeleted.getStarPoint();
			
		averageStarPoint = allStarPoint / (oldVisits - 1);
		}
		

		// update visits
		commentedSpot.setVisits(oldVisits - 1);
		// update usersStarPoint
		commentedSpot.setUsersStarPoint(averageStarPoint);
		//update reliabilit of spot
		if(commentedSpot.getUsersStarPoint() != -1)
		{
			float newRelability = 100-Math.abs(commentedSpot.getRegistrantStarPoint() - commentedSpot.getUsersStarPoint());
			commentedSpot.setReliability(newRelability); 
		}
		

		// Registrant's reliability updates every time a comment is posted
		// Average userStarPoint should be updated.
		updateRegistrantReliability(commentedSpot);
		
		
		//The delete Comment performed in the above logic is reflected in the DB before JPQL is executed.
		//if there is no comment for the spot written by the user
		//delete visitorVisitedSpot
		User commentUser = userService.findByAccountId(LoginUserAccountId).get();
		if(commentService.isNotPresentCommentOnSpotByUser(commentedSpot, commentUser))
		{
			Optional<VisitorVisitedSpot> visitorVisitedSpotOptional = visitorVisitedSpotService.findOneByUserAndSpot(commentUser, commentedSpot);
			if(visitorVisitedSpotOptional.isPresent())
			{
				VisitorVisitedSpot visitorVisitedSpot = visitorVisitedSpotService.findOneByUserAndSpot(commentUser, commentedSpot).get();
				visitorVisitedSpotService.delete(visitorVisitedSpot);
			}
		}

		return "redirect:/spot/spot/"+commentedSpot.getId();
	}
	
	
	@Transactional
	@PostMapping("/modify")
	public String modifyComment(@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId
			,@Valid @ModelAttribute CommentModificationForm commentModificationForm, BindingResult bindingResult) {
		
		
		
		//검증에 실패하면 다시 입력 폼으로
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "redirect:/spot/spot/" + commentModificationForm.getSpotId();
		}
		
		
		
		
		
		//성공 로직
		
		Spot commentedSpot = spotService.findById(commentModificationForm.getSpotId()).get();

		Comment commentToBeModified = commentService.findById(commentModificationForm.getCommentId()).get();
		
		//permission check
		//Confirm that logingUser is the person who wrote the comment
		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		if(!commentService.getAllCommentListByUser(loginUser).contains(commentToBeModified))
		{
			return "error/doNotHavePermission";
		}

		commentToBeModified.setComment(commentModificationForm.getComment());
		commentToBeModified.setStarPoint(commentModificationForm.getNewStarPoint());

		// Update Average Star Point and Registrant's Reliability
		Long oldVisits = commentedSpot.getVisits();
		float oldUsersStarPoint = commentedSpot.getUsersStarPoint();

	    float oldAllStarPoint = (oldUsersStarPoint * oldVisits);
	    float starPointChange = commentModificationForm.getNewStarPoint()-commentModificationForm.getOldStarPoint();
	    float newAllStarPoint = oldAllStarPoint + starPointChange;
	    Long newVisits = oldVisits; //old new same

		float newAverageStarPoint = newAllStarPoint / newVisits;

		// update visits
		commentedSpot.setVisits(newVisits);
		// update usersStarPoint
		commentedSpot.setUsersStarPoint(newAverageStarPoint);
		//updaate reliabilit of spot
		if(commentedSpot.getUsersStarPoint() != -1)
		{
			float newRelability = 100-Math.abs(commentedSpot.getRegistrantStarPoint() - commentedSpot.getUsersStarPoint());
			commentedSpot.setReliability(newRelability); 
		}

		// Registrant's reliability updates every time a comment is posted
		// Average userStarPoint should be updated.
		updateRegistrantReliability(commentedSpot);
		
		return "redirect:/spot/spot/"+commentModificationForm.getSpotId();
	}
	
	
	

	/*
	 * Registrant's reliability updates every time a comment is posted
	 */
	public void updateRegistrantReliability(Spot spot) {
		User registrant = spot.getRegistrant();
		List<Float> registerationSpotReliabilityListExcludingNotVisited = spotService.getRegisterationSpotReliabilityListExcludingNotVisited(registrant);
		if(registerationSpotReliabilityListExcludingNotVisited.isEmpty())
		{
			registrant.setReliability(-1);
		}
		else
		{
			float tatalReliability=0;
			float visitedspotNum=0;
			for(float reliabilityOfSpot : registerationSpotReliabilityListExcludingNotVisited)
			{
				tatalReliability = tatalReliability+reliabilityOfSpot;
				visitedspotNum++;
			}
			float newReliability = tatalReliability/visitedspotNum;
			registrant.setReliability(newReliability);
		}
		
		
	}

}
