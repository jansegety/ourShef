package my.ourShef.controller.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RecentSpot {

	private String spotName;
	private LocalDateTime registeredTime;
	private String spotIntroduction;
	
	private String mainImgStoreName;
	
	private int visits;
	private float registrantStarPoint;
	private float usersStarPoint;
}
