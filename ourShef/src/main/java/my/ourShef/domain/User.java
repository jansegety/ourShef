package my.ourShef.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class User {

	@Id @GeneratedValue
	@Column(name="USER_ID")
	private Long id;
	private String accountId;
	private Integer password;
	private String nickName;
	private String introduction;
//	UploadFile profileImg;
	private float reliability=-1; //default = -1, when set 0~100 (%)
	
	@OneToMany(mappedBy = "registrant")
	private List<Spot> registeredSpots=new ArrayList<>();
//	List<Spot> visitedSpots;
	
	public void addRegisteredSpots(Spot spot) {
		registeredSpots.add(spot);
		spot.setRegistrant(this);
	}
	
	
	
}
