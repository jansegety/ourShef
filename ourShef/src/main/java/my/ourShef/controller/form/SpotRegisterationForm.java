package my.ourShef.controller.form;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SpotRegisterationForm {

	private MultipartFile spotMainImgInfo;
	private List<MultipartFile> spotAddedImgsInfo;
	
	@NotBlank
	private String spotName;
	private String spotIntroduction;
	
	@Range(min = 10, max =100)
	private float starPoint; //최하 10 최대100
	
}
