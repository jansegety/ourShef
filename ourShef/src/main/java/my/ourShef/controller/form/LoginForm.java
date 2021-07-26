package my.ourShef.controller.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginForm {

	@NotEmpty
	private String loginFormId;
	
	@NotEmpty
	private String loginFormPassword;
	
}
