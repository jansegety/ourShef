package my.ourShef.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.dto.Acquaintance;
import my.ourShef.controller.dto.CommentDto;
import my.ourShef.controller.dto.RecentSpot;
import my.ourShef.controller.dto.RegistrantDto;
import my.ourShef.controller.dto.SpotDetailDto;
import my.ourShef.controller.form.SpotRegisterationForm;
import my.ourShef.controller.validator.SpotRegisterationFormValidator;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.service.CommentService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UserService;

@Slf4j
@Controller
@RequestMapping("/spot")
@RequiredArgsConstructor
public class SpotController {

	private final SpotRegisterationFormValidator spotRegisterationFormValidator;
	private final SpotService spotService;
	private final UserService userService;
	private final CommentService commentService;
	
	@InitBinder //요청이 올 때마다 dataBinder는 새로 만들어진다. //이 컨트롤러에서만 적용된다.
	public void init(WebDataBinder dataBinder) {
		
		if(dataBinder.getTarget() == null) return;
		
		final List<Validator> validatorsList = new ArrayList<>();
		validatorsList.add(spotRegisterationFormValidator);
		
		for(Validator validator : validatorsList) {
			if(validator.supports(dataBinder.getTarget().getClass())) {
				dataBinder.addValidators(validator);
			}
		}

		
	}
	
	@GetMapping("/registeration")
	public String createForm(Model model) {
		model.addAttribute("spotRegisterationForm", new SpotRegisterationForm());
		return "spot/spotRegisteration";
	}
	
	@PostMapping("/registeration")
	public String registerSpot(@Validated @ModelAttribute SpotRegisterationForm spotRegisterationForm, BindingResult bindingResult) {
		
		log.info("spotInfo={} ", spotRegisterationForm);
		
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "spot/spotRegisteration";
		}
		
		
		return "redirect:/";
	}
	
	@Transactional
	@GetMapping("/spot/{spotId}")
	public String spotDetail(@PathVariable("spotId") String spotId, Model model) {
		
		Optional<Spot> findSpotOptional = spotService.findById(Long.parseLong(spotId));
		Spot spot = findSpotOptional.get();
		
		setRegistrantDto(model, spot.getRegistrant());
		setSpotDetailDto(model, spot);
		setCommentDtoList(model, spot, 5, 0); //default limit 5 offset 0
		
		
		return "spot/spot";
	}
	
	/*
	 * Login User Spot List
	 */
	@GetMapping("/mySpotList")
	public String mySpotList(
			@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId, 
			Model model) {
		
		return "spot/spotListOfUser";
	}
	
	@GetMapping("/userSpotList/{userId}")
	public String userSpotList(@PathVariable("userId") String userId) {
		
		return "spot/spotListOfUser";
	}
	
	
	
	private void setCommentDtoList(Model model, Spot spot, int limit, int offset) {
		List<Comment> commentList = commentService.getCommentListBySpot(spot, limit, offset);
		List<CommentDto> commentDtoList = new ArrayList<CommentDto>();
		
		for(Comment comment : commentList) {
			CommentDto commentDto = new CommentDto();
			commentDto.setCommentUserNickName(comment.getCommentUser().getNickName());
			commentDto.setCommentUserProfileImgStoreName(comment.getCommentUser().getProfileImgInfo().getStoreFileName());
			
			//Divide comments into summary and detail
			//If it is less than 46 characters, it is not divided
			String commentText = comment.getComment();
			if(commentText.length() >= 46)
			{
				String commentSummary = comment.getComment().substring(0, 46);
				String commentDetatil = comment.getComment().substring(46);
				commentDto.setCommentSummary(commentSummary);
				commentDto.setCommentDetail(commentDetatil);
			}
			else {
				commentDto.setCommentSummary(commentText);
			}
			commentDto.setRegisteredTime(comment.getRegisteredTime());
			commentDto.setStarPoint(comment.getStarPoint());
			
			commentDtoList.add(commentDto);
		}
		
		model.addAttribute("commentDtoList", commentDtoList);
	}

	private void setRegistrantDto(Model model, User registrant) {
		
		RegistrantDto registrantDto = new RegistrantDto();
		registrantDto.setId(registrant.getId());
		registrantDto.setNickName(registrant.getNickName());
		registrantDto.setIntroduction(registrant.getIntroduction());
		registrantDto.setProfileImgStoreName(registrant.getProfileImgInfo().getStoreFileName());
		registrantDto.setReliability(registrant.getReliability());
		
		model.addAttribute("registrantDto", registrantDto);
		
	}

	private void setSpotDetailDto(Model model, Spot spot) {
		
		SpotDetailDto spotDetailDto = new SpotDetailDto();
		spotDetailDto.setId(spot.getId());
		spotDetailDto.setSpotName(spot.getSpotName());
		spotDetailDto.setSpotIntroduction(spot.getSpotIntroduction());
		spotDetailDto.setMainImgStoreName(spot.getMainSpotImgInfo().getStoreFileName());
		
		List<AddedSpotImg> addedSpotImgs = spot.getAddedSpotImgs();
		List<String> addedSpotImgStoreNames = new ArrayList<>();
		for(AddedSpotImg addedSpotImg : addedSpotImgs) {
			String storeFileName = addedSpotImg.getUploadFileInfo().getStoreFileName();
			addedSpotImgStoreNames.add(storeFileName);
		}
		spotDetailDto.setAddedImgStoreNames(addedSpotImgStoreNames);
		spotDetailDto.setRegisteredTime(spot.getRegisteredTime());
		spotDetailDto.setRegistrantStarPoint(spot.getRegistrantStarPoint());
		spotDetailDto.setUsersStarPoint(spot.getUsersStarPoint());
		spotDetailDto.setVisits(spot.getVisits());
		
		model.addAttribute("spotDetailDto", spotDetailDto);
	}
	
	
	
	
	
	
	
}
