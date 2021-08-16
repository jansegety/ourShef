package my.ourShef.controller.form;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SpotRegisterationForm {

	private MultipartFile spotMainImg;
	private List<MultipartFile> spotAddedImgs;
	
	@NotBlank
	@Size(max=20)
	private String spotName;
	
	@Size(max=400)
	private String spotIntroduction;
	
	@Range(min = 10, max =100)
	private float starPoint; //최하 10 최대100
	
}
