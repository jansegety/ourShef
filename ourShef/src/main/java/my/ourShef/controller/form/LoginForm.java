package my.ourShef.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {

	@NotEmpty
	@Size(max=20)
	private String loginFormId;
	
	@NotEmpty
	@Size(max=16)
	private String loginFormPassword;
	
}
