package my.ourShef.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.dto.Acquaintance;
import my.ourShef.controller.dto.CommentDto;
import my.ourShef.controller.dto.RecentSpot;
import my.ourShef.controller.dto.RegistrantDto;
import my.ourShef.controller.dto.SpotDetailDto;
import my.ourShef.controller.dto.UserSpotListSpotDto;
import my.ourShef.controller.dto.UserSpotListUserDto;
import my.ourShef.controller.form.CommentForm;
import my.ourShef.controller.form.CommentModificationForm;
import my.ourShef.controller.form.SpotModificationForm;
import my.ourShef.controller.form.SpotRegisterationForm;
import my.ourShef.controller.pager.SpotPager;
import my.ourShef.controller.validator.SpotModificationFormValidator;
import my.ourShef.controller.validator.SpotRegisterationFormValidator;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.file.FilePath;
import my.ourShef.file.FileStore;
import my.ourShef.service.CommentService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.AddedSpotImgService;
import my.ourShef.service.bridge.VisitorVisitedSpotService;

@Slf4j
@Controller
@RequestMapping("/spot")
@RequiredArgsConstructor
public class SpotController {

	private final CommentController commentController;
	private final SpotRegisterationFormValidator spotRegisterationFormValidator;
	private final SpotModificationFormValidator spotModificationFormValidator;
	private final SpotService spotService;
	private final UserService userService;
	private final CommentService commentService;
	private final FileStore fileStore;
	private final UploadFileInfoService uploadFileInfoService;
	private final AddedSpotImgService addedSpotImgService;
	private final VisitorVisitedSpotService visitorVisitedSpotService;

	@InitBinder // 요청이 올 때마다 dataBinder는 새로 만들어진다. //이 컨트롤러에서만 적용된다.
	public void init(WebDataBinder dataBinder) {

		if (dataBinder.getTarget() == null)
			return;

		final List<Validator> validatorsList = new ArrayList<>();
		validatorsList.add(spotRegisterationFormValidator);
		validatorsList.add(spotModificationFormValidator);

		for (Validator validator : validatorsList) {
			if (validator.supports(dataBinder.getTarget().getClass())) {
				dataBinder.addValidators(validator);
			}
		}

	}

	@GetMapping("/registeration")
	public String createForm(Model model) {
		model.addAttribute("spotRegisterationForm", new SpotRegisterationForm());
		return "spot/spotRegisteration";
	}

	@Transactional
	@PostMapping("/registeration")
	public String registerSpot(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = false) String LoginUserAccountId,
			@Validated @ModelAttribute SpotRegisterationForm spotRegisterationForm, BindingResult bindingResult)
			throws IOException {

		if (bindingResult.hasErrors()) {
			return "spot/spotRegisteration";
		}

		// 성공 로직
		User findUser = userService.findByAccountId(LoginUserAccountId).get();
		String spotName = spotRegisterationForm.getSpotName();
		Spot newSpot = new Spot(findUser, spotName);
		// persist
		spotService.save(newSpot);
		newSpot.setSpotIntroduction(spotRegisterationForm.getSpotIntroduction());
		newSpot.setRegistrantStarPoint(spotRegisterationForm.getStarPoint());

		// MainImg
		// SPOT_MAIN_IMG 경로에 Spot Main Img 저장
		UploadFileInfo mainImgInfo = fileStore.storeFile(spotRegisterationForm.getSpotMainImg(),
				FilePath.SPOT_MAIN_IMG);
		// UploadFileInfo 영속화
		uploadFileInfoService.save(mainImgInfo);
		// Spot + MaibImgInfo 연결
		newSpot.setMainSpotImgInfo(mainImgInfo);

		// AddedImg
		List<UploadFileInfo> spotAddesImgList = fileStore.storeFiles(spotRegisterationForm.getSpotAddedImgs(),
				FilePath.SPOT_ADDED_IMG);
		// UploadFileInfo 영속화
		uploadFileInfoService.saves(spotAddesImgList);

		// Bridge Entity
		// AddedSpotImg 생성 및 영속화
		List<AddedSpotImg> addedSpotImgList = addedSpotImgService.constructWithUploadFileInfoAndSpot(spotAddesImgList,
				newSpot);
		addedSpotImgService.saves(addedSpotImgList);

		return "redirect:/";
	}

	@Transactional
	@GetMapping("/spot/{spotId}")
	public String spotDetail(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = false) String LoginUserAccountId,
			@RequestParam(value = "page", defaultValue = "1") Long page, @PathVariable("spotId") String spotId,
			Model model) {

		Optional<Spot> findSpotOptional = spotService.findById(Long.parseLong(spotId));
		Spot spot = findSpotOptional.get();

		Optional<User> loginUserOptional = userService.findByAccountId(LoginUserAccountId);
		model.addAttribute("loginUserId", loginUserOptional.get().getId());

		setRegistrantDto(model, spot.getRegistrant());
		setSpotDetailDto(model, spot);
		setCommentDtoListAndPager(model, spot, 5L, 5L, page);

		return "spot/spot";
	}

	@Transactional
	@GetMapping("/userSpotList/{userId}")
	public String userSpotListByPage(@RequestParam(value = "page", defaultValue = "1") Long page,
			@PathVariable("userId") Long userId, Model model) {

		Optional<User> findUserOptional = userService.findById(userId);
		User findUser = findUserOptional.get();

		setUserSpotListUserDto(model, findUser);
		setUserSpotListSpotDto(model, findUser, 4L, 5L, page);

		return "spot/spotListOfUser";
	}

	@Transactional
	@GetMapping("/modification/{spotId}")
	public String createModificationForm(@PathVariable("spotId") Long spotId, Model model) {

		Spot findSpot = spotService.findById(spotId).get();

		// spot Img name setting
		setSpotImgNames(model, findSpot);

		// set Form
		SpotModificationForm spotModificationForm = new SpotModificationForm();

		spotModificationForm.setSpotId(spotId);
		spotModificationForm.setSpotName(findSpot.getSpotName());
		spotModificationForm.setSpotIntroduction(findSpot.getSpotIntroduction());
		spotModificationForm.setRegistrantStarPoint(findSpot.getRegistrantStarPoint());

		model.addAttribute("spotModificationForm", spotModificationForm);

		return "spot/spotModification";
	}

	@Transactional
	@PostMapping("/modification/{spotId}")
	public String modifySpot(Model model, @Validated @ModelAttribute SpotModificationForm spotModificationForm,
			BindingResult bindingResult, @PathVariable("spotId") Long spotId, RedirectAttributes redirectAttributes) throws IOException {
		
		
		Spot findSpot = spotService.findById(spotId).get();

		// spot Img name setting
		setSpotImgNames(model, findSpot);

		if (bindingResult.hasErrors()) {
			return "spot/spotModification";
		}

		// 성공 로직
		//If MainImg is Exist
		if (!spotModificationForm.getSpotMainImg().isEmpty()) {
			// delete Img file
			String oldSpotMainImgStoreName = findSpot.getMainSpotImgInfo().getStoreFileName();
			fileStore.deleteFile(oldSpotMainImgStoreName, FilePath.SPOT_MAIN_IMG);
			// delete uploadFileInfoService Entity in DB
			uploadFileInfoService.delete(findSpot.getMainSpotImgInfo());

			// MainImg
			// SPOT_MAIN_IMG 경로에 Spot Main Img 저장
			UploadFileInfo mainImgInfo = fileStore.storeFile(spotModificationForm.getSpotMainImg(),
					FilePath.SPOT_MAIN_IMG);
			// UploadFileInfo 영속화
			uploadFileInfoService.save(mainImgInfo);
			// Spot + MaibImgInfo 연결
			findSpot.setMainSpotImgInfo(mainImgInfo);
		}

		//If AddedImgs are Exist
		if(!spotModificationForm.getSpotAddedImgs().get(0).isEmpty())
		{
			//deleteFiles
			List<AddedSpotImg> oldAddedSpotImgs = findSpot.getAddedSpotImgs();			
			for(AddedSpotImg oldAddedSpotImg : oldAddedSpotImgs)
			{
				String oldAddedSpotImgFileName = oldAddedSpotImg.getUploadFileInfo().getStoreFileName();
				fileStore.deleteFile(oldAddedSpotImgFileName, FilePath.SPOT_ADDED_IMG);
			}
			
			//delete  uploadFileInfoService Entity in DB
			for(AddedSpotImg oldAddedSpotImg : oldAddedSpotImgs)
			{
				UploadFileInfo oldUploadFileInfo = oldAddedSpotImg.getUploadFileInfo();
				uploadFileInfoService.delete(oldUploadFileInfo);
			
			}
			
			//delete AddedSpotImg Entity in DB
			for(AddedSpotImg oldAddedSpotImg : oldAddedSpotImgs)
			{
				addedSpotImgService.delete(oldAddedSpotImg);
			}
			
			
			
			// AddedImg
			List<UploadFileInfo> spotAddesImgList = fileStore.storeFiles(spotModificationForm.getSpotAddedImgs(),
					FilePath.SPOT_ADDED_IMG);
			// UploadFileInfo 영속화
			uploadFileInfoService.saves(spotAddesImgList);
			// Bridge Entity
			// AddedSpotImg 생성 및 영속화
			List<AddedSpotImg> addedSpotImgList = addedSpotImgService.constructWithUploadFileInfoAndSpot(spotAddesImgList,
					findSpot);
			addedSpotImgService.saves(addedSpotImgList);
		}
		

		//update spot
		findSpot.setSpotName(spotModificationForm.getSpotName());
		findSpot.setSpotIntroduction(spotModificationForm.getSpotIntroduction());
		findSpot.setRegistrantStarPoint(spotModificationForm.getRegistrantStarPoint());
		
		
		//update Registrant's Reliability
		commentController.updateRegistrantReliability(findSpot);
		
		
		
		
		return "redirect:/spot/spot/"+spotId;
	}
	
	
	@Transactional
	@PostMapping("/delete")
	public String deleteSpot(@RequestParam(value = "spotId") Long spotId ) {
		
		////Checks if there is the Spot and returns home if not
		Optional<Spot> findSpotOptioanl = spotService.findById(spotId);
		if(findSpotOptioanl.isEmpty())
			return "redirect:/";
		Spot spotToBeDelted = findSpotOptioanl.get();
		
		////Delete all comment entities related to the spot
		List<Comment> comments = spotToBeDelted.getComments();
		//Check all the users who have visited by using the Set type that guarantees uniqueness
		Set<User> visitorSet = new HashSet<User>();
		
		for(Comment comment : comments)
		{		
			
			visitorSet.add(comment.getCommentUser());
			
			//delete CommentEntity in DB
			commentService.delete(comment);				
		}
		
		////Delete VisitorVisitedSpot Entity of users who have visited the spot
		for(User visitor : visitorSet)
		{
			Optional<VisitorVisitedSpot> vvsOptional = visitorVisitedSpotService.findOneByUserAndSpot(visitor, spotToBeDelted);
			if(vvsOptional.isPresent())
			{
				//delete VisitorVisitedSpot Entity
				visitorVisitedSpotService.delete(vvsOptional.get());
			}
			
		}
		
		////Registrant Reliability Updates
		User registrant = spotToBeDelted.getRegistrant();
		float oldReliability = registrant.getReliability();
		int rigisteredSpotsNum = registrant.getRegisteredSpots().size();
		float newReliability = 0;
		
		if(rigisteredSpotsNum > 1)
		{
			float oldReliabilitySum = oldReliability*rigisteredSpotsNum;
			float reliabilityOfSpotToBeDelted= 100 - Math.abs(spotToBeDelted.getRegistrantStarPoint() - spotToBeDelted.getUsersStarPoint());
			
			newReliability = (oldReliabilitySum - reliabilityOfSpotToBeDelted)/(rigisteredSpotsNum - 1);
		}
		else
		{
			newReliability = -1;
		}
		registrant.setReliability(newReliability);
		
		
		////Delete all IMG Etities associated with the spot
		////Delete all IMG files related to the spot
		
		//main
		UploadFileInfo mainSpotImgInfo = spotToBeDelted.getMainSpotImgInfo();
		String mainSpotImgStoreName = mainSpotImgInfo.getStoreFileName();
		//delete file 
		fileStore.deleteFile(mainSpotImgStoreName, FilePath.SPOT_MAIN_IMG);
		//delete Entity
		uploadFileInfoService.delete(mainSpotImgInfo);
		
		//added
		List<AddedSpotImg> addedSpotImgs = spotToBeDelted.getAddedSpotImgs();
		for(AddedSpotImg addedSpotImg : addedSpotImgs)
		{
			UploadFileInfo addedSpotImgInfo = addedSpotImg.getUploadFileInfo();
			String addedSpotImgStoreName = addedSpotImgInfo.getStoreFileName();
			
			//delete file 
			fileStore.deleteFile(addedSpotImgStoreName, FilePath.SPOT_ADDED_IMG);
			//delete Entity
			uploadFileInfoService.delete(addedSpotImgInfo);
			addedSpotImgService.delete(addedSpotImg);
		}
		
		
		////Delete Spot Entity
		spotService.delete(spotToBeDelted);
		
		return "redirect:/confirmation/deleteSpot";
	}
	

	// spot Img name setting
	private void setSpotImgNames(Model model, Spot findSpot) {
		// main Img
		String spotMainImgStoreName = findSpot.getMainSpotImgInfo().getStoreFileName();
		model.addAttribute("spotMainImgStoreName", spotMainImgStoreName);

		// added Img
		ArrayList<String> spotAddedImgStoreNames = new ArrayList<String>();
		List<AddedSpotImg> addedSpotImgs = findSpot.getAddedSpotImgs();
		for (AddedSpotImg addedSpotImg : addedSpotImgs) {
			String addedImgStoreName = addedSpotImg.getUploadFileInfo().getStoreFileName();
			spotAddedImgStoreNames.add(addedImgStoreName);
		}
		model.addAttribute("spotAddedImgStoreNames", spotAddedImgStoreNames);
	}

	private void setUserSpotListSpotDto(Model model, User findUser, Long tupleNumByPage, Long pageNumByGroup,
			Long currentPage) {

		Long totalTupleNum = spotService.getAllRegisteredSpotsNumByUser(findUser);

		SpotPager userSpotPager = new SpotPager(tupleNumByPage, pageNumByGroup, currentPage, totalTupleNum);
		model.addAttribute("userSpotPager", userSpotPager);

		Long offset = tupleNumByPage * (currentPage - 1); // because index is num - 1

		List<Spot> allRegisteredSpotsByUser = spotService.getRegisteredSpotsByUserUsingPaging(findUser, tupleNumByPage,
				offset);
		List<UserSpotListSpotDto> userSpotListSpotDtoList = new ArrayList<UserSpotListSpotDto>();

		for (Spot spot : allRegisteredSpotsByUser) {

			UserSpotListSpotDto userSpotListSpotDto = new UserSpotListSpotDto();

			userSpotListSpotDto.setId(spot.getId());
			userSpotListSpotDto.setMainImgStoreName(spot.getMainSpotImgInfo().getStoreFileName());
			userSpotListSpotDto.setRegisteredTime(spot.getRegisteredTime());
			userSpotListSpotDto.setRegistrantStarPoint(spot.getRegistrantStarPoint());
			userSpotListSpotDto.setSpotIntroduction(spot.getSpotIntroduction());
			userSpotListSpotDto.setSpotName(spot.getSpotName());
			userSpotListSpotDto.setUsersStarPoint(spot.getUsersStarPoint());
			userSpotListSpotDto.setVisits(spot.getVisits());

			userSpotListSpotDtoList.add(userSpotListSpotDto);

		}

		model.addAttribute("userSpotListSpotDtoList", userSpotListSpotDtoList);

	}

	private void setUserSpotListUserDto(Model model, User findUser) {

		UserSpotListUserDto userSpotListUserDto = new UserSpotListUserDto();

		userSpotListUserDto.setId(findUser.getId());
		userSpotListUserDto.setNickName(findUser.getNickName());
		userSpotListUserDto.setIntroduction(findUser.getIntroduction());
		userSpotListUserDto.setReliability(findUser.getReliability());
		userSpotListUserDto.setProfileImgStoreName(findUser.getProfileImgInfo().getStoreFileName());

		model.addAttribute("userSpotListUserDto", userSpotListUserDto);

	}

	private void setCommentDtoListAndPager(Model model, Spot spot, Long tupleNumByPage, Long pageNumByGroup,
			Long currentPage) {
		Long totalTupleNum = commentService.getAllCommentsNumBySpot(spot);
		SpotPager commentPager = new SpotPager(tupleNumByPage, pageNumByGroup, currentPage, totalTupleNum);
		model.addAttribute("commentPager", commentPager);

		Long offset = tupleNumByPage * (currentPage - 1); // because index is num - 1

		setCommentDtoList(model, spot, tupleNumByPage, offset);

	}

	private void setCommentDtoList(Model model, Spot spot, Long limit, Long offset) {
		List<Comment> commentList = commentService.getCommentListBySpot(spot, limit, offset);
		List<CommentDto> commentDtoList = new ArrayList<CommentDto>();

		for (Comment comment : commentList) {
			CommentDto commentDto = new CommentDto();
			commentDto.setCommentId(comment.getId());
			commentDto.setCommentUserNickName(comment.getCommentUser().getNickName());
			commentDto.setCommentUserId(comment.getCommentUser().getId());
			commentDto
					.setCommentUserProfileImgStoreName(comment.getCommentUser().getProfileImgInfo().getStoreFileName());

			// Divide comments into summary and detail
			// If it is less than 46 characters, it is not divided
			String commentText = comment.getComment();
			if (commentText.length() >= 46) {
				String commentSummary = comment.getComment().substring(0, 46);
				String commentDetatil = comment.getComment().substring(46);
				commentDto.setCommentSummary(commentSummary);
				commentDto.setCommentDetail(commentDetatil);
			} else {
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
		spotDetailDto.setRegistrantId(spot.getRegistrant().getId());
		spotDetailDto.setSpotName(spot.getSpotName());
		spotDetailDto.setSpotIntroduction(spot.getSpotIntroduction());
		spotDetailDto.setMainImgStoreName(spot.getMainSpotImgInfo().getStoreFileName());

		List<AddedSpotImg> addedSpotImgs = spot.getAddedSpotImgs();
		List<String> addedSpotImgStoreNames = new ArrayList<>();
		for (AddedSpotImg addedSpotImg : addedSpotImgs) {
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
