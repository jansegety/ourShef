package my.ourShef.controller.dto;

import lombok.Data;

@Data
public class Acquaintance {

	String nickName;
	String profileImgStoreName;
	String introduction;
	private float reliability;
}
