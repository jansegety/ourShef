package my.ourShef.controller.dto;

import lombok.Data;

@Data
public class AcquaintanceDto {

	Long id;
	String nickName;
	String profileImgStoreName;
	private float reliability;
	
}
