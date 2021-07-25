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

	@Override
	public boolean supports(Class<?> clazz) {
		return JoinForm.class.isAssignableFrom(clazz);
		// clazz가 JoinForm의 자식 클래스이거나 같은 클래스인가? 맞으면 true
	}

	@Override // Errors는 BindingResult의 부모 클래스
	public void validate(Object target, Errors errors) {

		JoinForm joinForm = (JoinForm) target;
		
		//중복된 AccountId가 있는지 검증
		try {
			us.validateDuplicateAccountId(joinForm.getJoinFormAccountId());
		} catch (Exception e) {
			//e.printStackTrace();
			errors.rejectValue("joinFormAccountId", "duplication");
		}
		

		if (joinForm.getJoinFormProfileImgFile().isEmpty()) {
			// 프로필 이미지 파일이 들어오지 않은 경우
			errors.rejectValue("joinFormProfileImgFile", "required");

		} else {
			// 프로필 이미지 파일이 이미지 파일이 아닌 경우
			String temp = joinForm.getJoinFormProfileImgFile().getOriginalFilename(); // ex 7.jpg
			String ext = temp.substring(temp.lastIndexOf(".") + 1); // 확장자 얻기
			String lowerCaseExt = ext.toLowerCase();
			if (!lowerCaseExt.equals("jpg") && !lowerCaseExt.equals("png")) {
				errors.rejectValue("joinFormProfileImgFile",
						"only.img.org.springframework.web.multipart.MultipartFile");
			}

		}
		
		//닉네임이 필수인데 들어오지 않은 경우
		if (!StringUtils.hasText(joinForm.getJoinFormNickName())) {
			errors.rejectValue("joinFormNickName", "required");
		}

		if (!StringUtils.hasText(joinForm.getJoinFormPassword()) || joinForm.getJoinFormPassword().length() > 16
				|| joinForm.getJoinFormPassword().length() < 8) {// 비밀번호가 제대로 입력되지 않은 경우
			errors.rejectValue("joinFormPassword", "range", new Object[] { 8, 16 }, null);
		} else {// 비밀번호가 입력되었지만 비밀번호 확인 번호와 다를 경우
			if (!joinForm.getJoinFormPassword().equals(joinForm.getJoinFormConfirmPassword())) {
				errors.rejectValue("joinFormPassword", "notEqal", null, null);
			}

		}
		
		//자기소개가 100자보다 많은 경우
		if (joinForm.getJoinFormSelfIntroduction().length() > 100) {
			errors.rejectValue("joinFormSelfIntroduction", "min.java.lang.String", new Object[] { 100 }, null);
		}

		// 특정 필드가 아닌 복합 룰 검증
		// 패스워드에 닉네임을 포함하고 있다면
		if (StringUtils.hasText(joinForm.getJoinFormPassword())
				&& StringUtils.hasText(joinForm.getJoinFormNickName())) {
			if (joinForm.getJoinFormPassword().contains(joinForm.getJoinFormNickName())) {
				errors.reject("dont.passwordContainsNickName", null, null);
			}
		}


	}

}
