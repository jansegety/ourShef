package my.ourShef.controller.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LoginUserRecentSpotDto {

	private LocalDateTime registeredTime;
	
	private String spotName;
	
	private String spotIntroduction;

	private float registrantStarPoint;
	
    //If -1, no one has visited.
    //10~100
	private float usersStarPoint;
}
