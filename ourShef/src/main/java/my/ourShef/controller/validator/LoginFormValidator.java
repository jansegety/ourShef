package my.ourShef.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import my.ourShef.controller.form.LoginForm;
import my.ourShef.domain.User;
import my.ourShef.service.LoginService;



@Component
@RequiredArgsConstructor
public class LoginFormValidator implements Validator{
	
	private final LoginService loginService;

	@Override
	public boolean supports(Class<?> clazz) {
		
		return LoginForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		LoginForm loginForm = (LoginForm)target;

		
		//if ID PASSWORD are inserted all
		if(StringUtils.hasText(loginForm.getLoginFormId())||StringUtils.hasText(loginForm.getLoginFormPassword()))
		{
			
			User loginUser = loginService.login(loginForm.getLoginFormId(), loginForm.getLoginFormPassword());
			//if loginUser is null 
			if(loginUser == null) {
				errors.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			}
		}
		
	}

	
}
