package my.ourShef.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Spot {

	@Id @GeneratedValue
	private Long id;
	private String spotName;
	private UploadFile mainSpotImg;
	private List<UploadFile> addedSpotImgs;
	private String spotIntroduction;
//	private List<Comment> comments;
	private User registrant;
	private List<User> visitors;
	private int visitedNumber;
	private float registrantStarPoint;
	private float usersStarPoint;
	
	
}
