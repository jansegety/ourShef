package my.ourShef.controller.dto;

import lombok.Data;

@Data
public class RegistrantDto {

	Long id;
	String nickName;
	String profileImgStoreName;
	String introduction;
	private float reliability;
	
}
