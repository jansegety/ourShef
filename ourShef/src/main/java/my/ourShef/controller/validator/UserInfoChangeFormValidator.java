package my.ourShef.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import my.ourShef.controller.form.UserInfoChangeForm;

@Component
public class UserInfoChangeFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {

		return UserInfoChangeForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		UserInfoChangeForm userInfoChangeForm = (UserInfoChangeForm) target;

		// Image file verification
		// When receive a changed profile image
		if (!userInfoChangeForm.getProfileImgFile().isEmpty()) {
			// If the profile image file is not an image file
			String temp = userInfoChangeForm.getProfileImgFile().getOriginalFilename(); // ex 7.jpg
			String ext = temp.substring(temp.lastIndexOf(".") + 1); // 확장자 얻기
			String lowerCaseExt = ext.toLowerCase();
			if (!lowerCaseExt.equals("jpg") && !lowerCaseExt.equals("png")) {
				errors.rejectValue("profileImgFile", "only.img.org.springframework.web.multipart.MultipartFile");
			}

		}

		// If a nickname is required but it is not entered
		if (!StringUtils.hasText(userInfoChangeForm.getNickName())) {
			errors.rejectValue("nickName", "required");
		}

		// Password Verification
		if (!StringUtils.hasText(userInfoChangeForm.getPassword()) || userInfoChangeForm.getPassword().length() > 16
				|| userInfoChangeForm.getPassword().length() < 8) {// If the password is not entered correctly
			errors.rejectValue("password", "range", new Object[] { 8, 16 }, null);
		}

		//If change Password
		if(StringUtils.hasText(userInfoChangeForm.getNewPassword()))
		{
			// NewPassword Verification
			if (userInfoChangeForm.getNewPassword().length() > 16
					|| userInfoChangeForm.getNewPassword().length() < 8) {// If the new password is not entered correctly
				errors.rejectValue("newPassword", "range", new Object[] { 8, 16 }, null);
			} else {// If a new password is entered but it is different from the password
					// confirmation number
				if (!userInfoChangeForm.getNewPassword().equals(userInfoChangeForm.getConfirmNewPassword())) {
					errors.rejectValue("newPassword", "notEqal", null, null);
				}
			}
		}
				

		// Introduction Verification
		// Self-introduction more than 100 characters
		if (userInfoChangeForm.getSelfIntroduction().length() > 100) {
			errors.rejectValue("selfIntroduction", "min.java.lang.String", new Object[] { 100 }, null);
		}

		/* Complex Rule Validation */
		// If includes a nickName it the newPassword 
		if (StringUtils.hasText(userInfoChangeForm.getNewPassword())
				&& StringUtils.hasText(userInfoChangeForm.getNickName())) {
			if (userInfoChangeForm.getPassword().contains(userInfoChangeForm.getNickName())) {
				errors.reject("dont.passwordContainsNickName", null, null);
			}
		}
		// If include Account ID in the newPassword
		if (StringUtils.hasText(userInfoChangeForm.getNewPassword())
				&& StringUtils.hasText(userInfoChangeForm.getAccountId())) {
			if (userInfoChangeForm.getPassword().contains(userInfoChangeForm.getAccountId())) {
				errors.reject("dont.passwordContainsAccountId", null, null);
			}
		}

	}

}
