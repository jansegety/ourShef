package my.ourShef.controller.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SpotDetailDto {

	private Long id;
	private String spotName;
	private LocalDateTime registeredTime;
	private String spotIntroduction;
	
	private String mainImgStoreName;
	private List<String> addedImgStoreNames = new ArrayList<>();
	
	private Long visits;
	private float registrantStarPoint;
	private float usersStarPoint;
}
