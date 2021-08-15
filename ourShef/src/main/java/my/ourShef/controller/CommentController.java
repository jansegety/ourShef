package my.ourShef.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.form.CommentForm;
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

	@Transactional
	@PostMapping("/post")
	public String postComment(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			@ModelAttribute CommentForm commentForm) {

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

		// Registrant's reliability updates every time a comment is posted
		// Average userStarPoint should be updated.
		updateRegistrantReliability(commentedSpot);

		// updated as a visitor
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
		//Remove from persistence context
		commentService.delete(commentToBeDeleted);
		
		Spot commentedSpot = commentToBeDeleted.getCommentedSpot();

		// Update Average Star Point and Registrant's Reliability
		Long oldVisits = commentedSpot.getVisits();
		float oldUsersStarPoint = commentedSpot.getUsersStarPoint();

		float allStarPoint = 0;
		
		allStarPoint = (oldUsersStarPoint * oldVisits) - commentToBeDeleted.getStarPoint();
		

		float averageStarPoint = allStarPoint / (oldVisits - 1);

		// update visits
		commentedSpot.setVisits(oldVisits - 1);
		// update usersStarPoint
		commentedSpot.setUsersStarPoint(averageStarPoint);

		// Registrant's reliability updates every time a comment is posted
		// Average userStarPoint should be updated.
		updateRegistrantReliability(commentedSpot);
		
		

		return "redirect:/spot/spot/"+commentedSpot.getId();
	}

	/*
	 * Registrant's reliability updates every time a comment is posted
	 */
	private void updateRegistrantReliability(Spot spot) {
		User registrant = spot.getRegistrant();
		// If the registrant has never received a comment
		if (registrant.getReliability() == -1) {
			float newReliability = (100 - Math.abs(spot.getRegistrantStarPoint() - spot.getUsersStarPoint()));
			registrant.setReliability(newReliability);
		} else {

			Long registerationSpotNum = spotService.getCountRegisterationSpotNum(registrant);
			float oldReliability = registrant.getReliability();
			float oldReliabilitySum = (registerationSpotNum - 1) * oldReliability;

			float newReliability = (100 - Math.abs(spot.getRegistrantStarPoint() - spot.getUsersStarPoint()));

			float newAverageReliability = (oldReliabilitySum + newReliability) / registerationSpotNum;

			registrant.setReliability(newAverageReliability);
		}

	}

}
