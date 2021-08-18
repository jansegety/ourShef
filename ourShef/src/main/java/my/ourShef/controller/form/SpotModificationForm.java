package my.ourShef.controller.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SpotModificationForm {

	private MultipartFile spotMainImg;
	private List<MultipartFile> spotAddedImgs;
	
	@NotBlank
	private String spotName;
	
	@Size(max = 400)
	private String spotIntroduction;
	
	@Range(min = 10, max =100)
	private float registrantStarPoint; //최하 10 최대100
}
