package my.ourShef.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.form.LoginForm;
import my.ourShef.controller.form.UserInfoChangeForm;
import my.ourShef.controller.form.WithdrawForm;
import my.ourShef.controller.validator.JoinFormValidator;
import my.ourShef.controller.validator.LoginFormValidator;
import my.ourShef.domain.RelationshipRequest;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.domain.constant.RelationshipRequestState;
import my.ourShef.file.FilePath;
import my.ourShef.file.FileStore;
import my.ourShef.repository.UserRepository;
import my.ourShef.service.LoginService;
import my.ourShef.service.RelationshipRequestService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.UserAcquaintanceService;

@Controller
@RequestMapping("/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

	private final JoinFormValidator joinFormValidator;
	private final LoginFormValidator loginFormValidator;
	private final FileStore fileStore;
	
	private final AcquaintanceController acquaintanceController;
	private final SpotController spotController;
	
	private final UploadFileInfoService uploadFileInfoService;
	private final UserAcquaintanceService userAcquaintanceService;
	private final UserService userService;
	private final SpotService spotService;
	private final RelationshipRequestService relationshipRequestService;

	// @IntiBinder->해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.
	// 요청이 올 때마다 dataBinder는 새로 만들어진다.
	// 이 컨트롤러에서만 적용된다.
	@InitBinder
	public void init(WebDataBinder dataBinder) {

		if (dataBinder.getTarget() == null)
			return;

		final List<Validator> validatorsList = new ArrayList<>();
		validatorsList.add(joinFormValidator);
		validatorsList.add(loginFormValidator);

		for (Validator validator : validatorsList) {
			if (validator.supports(dataBinder.getTarget().getClass())) {
				dataBinder.addValidators(validator);
			}
		}

	}

	@GetMapping("/join")
	public String createForm(Model model) {
		model.addAttribute("joinForm", new JoinForm());
		return "login/joinForm";
	}

	
	@Transactional
	@PostMapping("/join")
	public String create(@Valid @ModelAttribute JoinForm joinForm, BindingResult bindingResult) throws IOException {

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "login/joinForm";
		}

		// 성공 로직
		User user = new User(joinForm.getJoinFormAccountId());
		user.setPassword(joinForm.getJoinFormPassword());
		user.setNickName(joinForm.getJoinFormNickName());
		user.setIntroduction(joinForm.getJoinFormSelfIntroduction());

		// USER_PROFILE_IMG 경로에 프로필 이미지 저장
		UploadFileInfo storeFile = fileStore.storeFile(joinForm.getJoinFormProfileImgFile(), FilePath.USER_PROFILE_IMG);
		// UploadFileInfo 영속화
		uploadFileInfoService.save(storeFile);

		// User + ProfileImgInfo 연결
		user.setProfileImgInfo(storeFile);

		try {
			userService.join(user);
		} catch (Exception e) {
			// 중복된 accountid가 존재할 경우
			bindingResult.rejectValue("joinFormAccountId", "duplication");
			System.out.println("두번째 중복 검증에서 걸림" + e.getMessage());
			return "login/joinForm";
		}

		return "redirect:/confirmation/createAccount";
	}

	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login/loginForm";
	}

	
	@PostMapping("/login")
	public String login(@Validated @ModelAttribute LoginForm loginform, BindingResult bindingResult,
			HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURL) {

		if (bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "login/loginForm";
		}

		/*
		 * servlet에서 제공하는 session 사용 accountId 정보를 세션에 저장
		 */
		// request.getSession(true) : default 세션이 없으면 새로 생성, 있으면 있는 것을 반환
		// request.getSession(false) : 세션이 있으면 기존 세션을 반환, 없으면 새로 생성하지 않고 null 반환
		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID, loginform.getLoginFormId());

		// redirectURL는 유저가 세션없이 로그인을 했을 때 접속을 원했던 페이지로 로그인 성공시 다시 보내주기 위한 조치
		return "redirect:" + redirectURL;

	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {

		/*
		 * servlet 세션 사용
		 */
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // 세션 무효화
		}

		return "redirect:/";

	}

	
	@GetMapping("/withdraw")
	public String createWithdrawForm(Model model) {
		
		model.addAttribute("withdrawForm", new WithdrawForm());
		
		return "login/withdrawForm";
	}
	
	@Transactional
	@PostMapping("/withdraw")
	public String withdraw(@SessionAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID) String LoginUserAccountId,
			@Valid @ModelAttribute WithdrawForm withdrawForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
		
		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		
		// If the passwords do not match
		if (!loginUser.getPassword().equals(withdrawForm.getPassword())) {
			bindingResult.rejectValue("password", "notMatch");
		}
		
		// If validation fails, return to the modification form.
		if (bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "login/withdrawForm";
		}
	
		//TODO List
		////1. Delete all acquaintance relationship data///////////
		//Delete Comment Entity from spot & Spot user average star rating update
		//The Spot Registrant Reliability Updates
		//Delete VisitorVisited Entity
		//Delete UserAcquaintance Entity
		////////////////////////////////////////////////////////
		List<User> findAcquaintanceList = userAcquaintanceService.findAcquaintanceByUser(loginUser);
		List<Long> findAcquaintanceIds = findAcquaintanceList.stream().map(e->e.getId()).collect(Collectors.toList());
		acquaintanceController.deleteAcquaintance(LoginUserAccountId, findAcquaintanceIds);
		
		
		////2. Delete all loginUser's spot relationship data/////////
		//Delete all comment entities related to the spot
		//Delete VisitorVisitedSpot Entity of users who have visited the spot
		//Delete all IMG Etities associated with the spot
		//Delete all IMG files related to the spot
		List<Spot> allRegisteredSpotsByUser = spotService.getAllRegisteredSpotsByUser(loginUser);
		for(Spot spotToBeDeleted : allRegisteredSpotsByUser)
		{
			spotController.deleteSpot(spotToBeDeleted.getId());
		}
		
		//3. Delete all relationshipRequest Entity
		List<RelationshipRequest> sendedRelationshipRequestList = relationshipRequestService.getSendedRelationshipRequest(loginUser, loginUser);
		sendedRelationshipRequestList.stream().forEach(relationshipRequestService::delete);
		List<RelationshipRequest> receivedRelationshipRequestList = relationshipRequestService.getReceivedRelationshipRequest(loginUser, loginUser);
		for(RelationshipRequest receivedRelationshipRequest : receivedRelationshipRequestList)
		{
			if(receivedRelationshipRequest.getState()==RelationshipRequestState.BEFORE_CONFIRMATION)
			{
				String opponentRequestId = receivedRelationshipRequest.getOpponentId();
				Optional<RelationshipRequest> relationshipRequestOptional = relationshipRequestService.findById(LoginUserAccountId);
				if(relationshipRequestOptional.isPresent())
				{
					relationshipRequestOptional.get().setState(RelationshipRequestState.REJECTED);
				}
			}
			relationshipRequestService.delete(receivedRelationshipRequest);
		}
			
		//4. Delete User Entity
		userService.delete(loginUser);
		
		//5. Session Invalidation
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		
		
		return "redirect:/confirmation/deleteAccount";
	}

}
