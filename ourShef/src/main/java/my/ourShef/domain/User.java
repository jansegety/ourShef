package my.ourShef.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import my.ourShef.domain.bridge.VisitorVisitedSpot;

@Entity
@Table(name="user")
@Data
public class User {

	@Id @GeneratedValue
	@Column(name="user_id")
	private Long id;
	@Column(name="account_id")
	private String accountId;
	private Integer password;
	@Column(name="nick_name")
	private String nickName;
	private String introduction;
	
	@JoinColumn(name="good")
	@OneToOne
	private UploadFileInfo profileImgInfo;
	
	
	private float reliability=-1; //default = -1, when set 0~100 (%)
	
	@OneToMany(mappedBy = "registrant")
	private List<Spot> registeredSpots=new ArrayList<>();
	
//	List<Spot> visitedSpots;
	@OneToMany(mappedBy="visitor")
	private List<VisitorVisitedSpot> visitorVisitedSpots=new ArrayList<>();;
	
	public void addRegisteredSpots(Spot spot) {
		registeredSpots.add(spot);
		spot.setRegistrant(this);
	}
	
	
	
}
