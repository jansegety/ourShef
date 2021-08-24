package my.ourShef.controller.form;

import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class WithdrawForm {

	@Size(max=16)
	private String password;
	
}
