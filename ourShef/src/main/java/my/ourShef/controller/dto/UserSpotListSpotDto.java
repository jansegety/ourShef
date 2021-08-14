package my.ourShef.controller.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserSpotListSpotDto {

	private Long id;
	private String spotName;
	private LocalDateTime registeredTime;
	private String spotIntroduction;
	
	private String mainImgStoreName;
	
	private Long visits;
	private float registrantStarPoint;
	private float usersStarPoint;
}
