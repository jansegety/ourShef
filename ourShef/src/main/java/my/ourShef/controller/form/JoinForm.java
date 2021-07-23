package my.ourShef.controller.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class JoinForm {
	
	private MultipartFile joinProfileImg;
	private String accountId;
	private String password;
	private String confirmPassword;
	private String nickName;
	private String selfIntroduction;
	
	
}
