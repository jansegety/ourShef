package my.ourShef.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.form.UserInfoChangeForm;
import my.ourShef.controller.validator.UserInfoChangeFormValidator;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.file.FilePath;
import my.ourShef.file.FileStore;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserInfoChangeFormValidator userInfoChangeFormValidator;
	
	private final FileStore fileStore;

	private final UserService userService;
	private final UploadFileInfoService uploadFileInfoService;

	// @IntiBinder->해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.
	// 요청이 올 때마다 dataBinder는 새로 만들어진다.
	// 이 컨트롤러에서만 적용된다.
	@InitBinder
	public void init(WebDataBinder dataBinder) {

		if (dataBinder.getTarget() == null)
			return;

		final List<Validator> validatorsList = new ArrayList<>();
		validatorsList.add(userInfoChangeFormValidator);

		for (Validator validator : validatorsList) {
			if (validator.supports(dataBinder.getTarget().getClass())) {
				dataBinder.addValidators(validator);
			}
		}

	}

	@Transactional
	@GetMapping("/myInfo")
	public String myInfo(@SessionAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID) String LoginUserAccountId, Model model) {

		// Registration date setting
		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		LocalDateTime registeredTime = loginUser.getRegisteredTime();
		model.addAttribute("registeredTime", registeredTime);

		// set UserInfoChangeForm
		UserInfoChangeForm userInfoChangeForm = new UserInfoChangeForm();
		userInfoChangeForm.setAccountId(LoginUserAccountId);
		userInfoChangeForm.setNickName(loginUser.getNickName());
		userInfoChangeForm.setSelfIntroduction(loginUser.getIntroduction());

		model.addAttribute("userInfoChangeForm", userInfoChangeForm);

		return "user/loginUserInfo";
	}

	@Transactional
	@PostMapping("/myInfo")
	public String modifyMyInfo(@SessionAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID) String LoginUserAccountId,
			@Valid @ModelAttribute UserInfoChangeForm userInfoChangeForm, BindingResult bindingResult, Model model) throws IOException {

		User loginUser = userService.findByAccountId(LoginUserAccountId).get();
		// Registration date setting
		LocalDateTime registeredTime = loginUser.getRegisteredTime();
		model.addAttribute("registeredTime", registeredTime);

		// If the passwords do not match
		if (!loginUser.getPassword().equals(userInfoChangeForm.getPassword())) {
			bindingResult.rejectValue("password", "notMatch");
		}

		// If validation fails, return to the modification form.
		if (bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "user/loginUserInfo";
		}
		
		/* success logic */
		//update nickName
		loginUser.setNickName(userInfoChangeForm.getNickName());
		
		//update profileImg
		if(!userInfoChangeForm.getProfileImgFile().isEmpty()) {
			
			String oldProfileImgFileName = loginUser.getProfileImgInfo().getStoreFileName();
			
			//delete old ProfileImgFile
			fileStore.deleteFile(oldProfileImgFileName, FilePath.USER_PROFILE_IMG);
			//delete uploadFileInfoService Entity in DB
			uploadFileInfoService.delete(loginUser.getProfileImgInfo());
				
			//Save new profile image to USER_PROFILE_IMG path
			UploadFileInfo storeFile = fileStore.storeFile(userInfoChangeForm.getProfileImgFile(), FilePath.USER_PROFILE_IMG); 
			//UploadFileInfo persist
			uploadFileInfoService.save(storeFile);
			//User + ProfileImgInfo
			loginUser.setProfileImgInfo(storeFile);
			
		}
		
		//update password
		if(StringUtils.hasText(userInfoChangeForm.getNewPassword()))
		{
			loginUser.setPassword(userInfoChangeForm.getNewPassword());
		}
		
		//update introduction
		loginUser.setIntroduction(userInfoChangeForm.getSelfIntroduction());
		

		return "redirect:/";

	}

}
