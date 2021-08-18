package my.ourShef.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserInfoChangeForm {

	private MultipartFile profileImgFile;

	@Size(max = 20)
	@NotBlank
	private String accountId;

	@Size(max = 16)
	private String newPassword;

	@Size(max = 16)
	private String confirmNewPassword;
	
	@Size(max = 16)
	private String password;

	@Size(max = 20)
	private String nickName;
	private String selfIntroduction;
}
