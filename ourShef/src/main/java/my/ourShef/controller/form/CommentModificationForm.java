package my.ourShef.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class CommentModificationForm {
	
	
	Long CommentId;
	
	Long spotId;
	
	
	@Range(min = 10, max =100)
	float oldStarPoint;
	
	@Range(min = 10, max =100)
	float newStarPoint;
	
	@Size(max=200)
	String comment;
	
}
