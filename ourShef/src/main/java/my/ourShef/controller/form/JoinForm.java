package my.ourShef.controller.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class JoinForm {
	
	private MultipartFile joinFormProfileImgFile;
	private String joinFormAccountId;
	private String joinFormPassword;
	private String joinFormConfirmPassword;
	private String joinFormNickName;
	private String joinFormSelfIntroduction;
	
	
}
