package my.ourShef.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.service.UserService;

@Component
@RequiredArgsConstructor
public class JoinFormValidator implements Validator {

	private final UserService us;
	private final FileExtValidator fileExtValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return JoinForm.class.isAssignableFrom(clazz);
		// clazz가 JoinForm의 자식 클래스이거나 같은 클래스인가? 맞으면 true
	}

	@Override // Errors는 BindingResult의 부모 클래스
	public void validate(Object target, Errors errors) {

		JoinForm joinForm = (JoinForm) target;

		// Validate for duplicate AccountId
		try {
			us.validateDuplicateAccountId(joinForm.getJoinFormAccountId());
		} catch (Exception e) {
			// e.printStackTrace();
			errors.rejectValue("joinFormAccountId", "duplication");
		}

		// Image file verification
		if (joinForm.getJoinFormProfileImgFile().isEmpty()) {
			// If the profile image file does not come in
			errors.rejectValue("joinFormProfileImgFile", "required");

		} else {
			// If the profile image file is not an image file
			String temp = joinForm.getJoinFormProfileImgFile().getOriginalFilename(); // ex 7.jpg
			String ext = temp.substring(temp.lastIndexOf(".") + 1); // 확장자 얻기
			String lowerCaseExt = ext.toLowerCase();
			if (fileExtValidator.isNotImgFile(lowerCaseExt)) {
				errors.rejectValue("joinFormProfileImgFile",
						"only.img.org.springframework.web.multipart.MultipartFile");
			}

		}

		// If a nickname is required but it is not entered
		if (!StringUtils.hasText(joinForm.getJoinFormNickName())) {
			errors.rejectValue("joinFormNickName", "required");
		}

		if (!StringUtils.hasText(joinForm.getJoinFormPassword()) || joinForm.getJoinFormPassword().length() > 16
				|| joinForm.getJoinFormPassword().length() < 8) {// If the password is not entered correctly
			errors.rejectValue("joinFormPassword", "range", new Object[] { 8, 16 }, null);
		} else {// If the password is entered but it is different from the password confirmation number
			if (!joinForm.getJoinFormPassword().equals(joinForm.getJoinFormConfirmPassword())) {
				errors.rejectValue("joinFormPassword", "notEqal", null, null);
			}

		}

		// Introduction Verification
		// Self-introduction more than 100 characters
		if (joinForm.getJoinFormSelfIntroduction().length() > 100) {
			errors.rejectValue("joinFormSelfIntroduction", "min.java.lang.String", new Object[] { 100 }, null);
		}

		/* Complex Rule Validation */
		// If includes a nickName it the newPassword 
		if (StringUtils.hasText(joinForm.getJoinFormPassword())
				&& StringUtils.hasText(joinForm.getJoinFormNickName())) {
			if (joinForm.getJoinFormPassword().contains(joinForm.getJoinFormNickName())) {
				errors.reject("dont.passwordContainsNickName", null, null);
			}
		}

		// If include Account ID in the newPassword
		if (StringUtils.hasText(joinForm.getJoinFormPassword())
				&& StringUtils.hasText(joinForm.getJoinFormAccountId())) {
			if (joinForm.getJoinFormPassword().contains(joinForm.getJoinFormAccountId())) {
				errors.reject("dont.passwordContainsAccountId", null, null);
			}
		}

	}

	

}
