package my.ourShef.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {

	@Id @GeneratedValue
	private Long id;
	private String userId;
	private Integer password;
	private String nickName;
	private String introduction;
//	UploadFile profileImg;
	float reliability;
//	List<Spot> registeredSpots
//	List<Spot> visitedSpots
	
	
}
