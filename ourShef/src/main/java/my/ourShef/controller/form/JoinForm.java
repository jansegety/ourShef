package my.ourShef.controller.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class JoinForm {
	
	private MultipartFile joinFormProfileImgFile;
	
	@Size(max=20)
	private String joinFormAccountId;
	
	private String joinFormPassword;
	
	@Size(max=16)
	private String joinFormConfirmPassword;
	
	
	@Size(max=20)
	private String joinFormNickName;
	private String joinFormSelfIntroduction;
	
	
}
