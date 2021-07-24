package my.ourShef.controller.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class JoinForm {
	
	private MultipartFile joinFormProfileImgFile;
	private String joinFormAccountId;
	
	//@NotBlank
	//@Range(min = 8, max = 16)
	private String joinFormPassword;
	private String joinFormConfirmPassword;
	//@NotBlank
	private String joinFormNickName;
	private String joinFormSelfIntroduction;
	
	
}
