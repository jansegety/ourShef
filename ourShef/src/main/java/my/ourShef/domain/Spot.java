package my.ourShef.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.domain.bridge.VisitorVisitedSpot;

//@SequenceGenerator(name = "SPOT_SEQ_GENERATOR", sequenceName = "SPOT_SEQ", initialValue = 1, allocationSize = 1)
@Entity
@Getter
@Table(name = "spot")
public class Spot {
	

	@Id
	@Column(name="spot_id")
	@GeneratedValue
	private Long id;
	
	
	@Column(name="registered_time")
	private LocalDateTime registeredTime;
	
	
	@Setter
	@Column(name="spot_name")
	private String spotName;
	
	@Setter
	@OneToOne
	@JoinColumn(name="main_spot_img_info")
	private UploadFileInfo mainSpotImgInfo;
	

	@Setter
	@Column(columnDefinition = "TEXT")
	private String spotIntroduction;

	@OneToMany(mappedBy="commentedSpot")
	private List<Comment> comments =new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User registrant;
	
	
    @OneToMany(mappedBy="visitedSpot")
	private List<VisitorVisitedSpot> visitorVisitedSpots=new ArrayList<>();
	
	
    @Setter
	@Column(name="registrant_star_point")
	private float registrantStarPoint;
	
    @Setter
	@Column(name="users_star_point")
	private float usersStarPoint;
	
	
	@OneToMany(mappedBy="spot")
	private List<AddedSpotImg> addedSpotImgs = new ArrayList<>();
	
	
	protected Spot() {
		
	}
	
	public Spot(User user, String spotName) {
		this.registrant = user;
		this.spotName = spotName;
	}
		
	
	
}
