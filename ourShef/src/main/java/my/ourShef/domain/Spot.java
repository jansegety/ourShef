package my.ourShef.domain;

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
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.domain.bridge.VisitorVisitedSpot;

//@SequenceGenerator(name = "SPOT_SEQ_GENERATOR", sequenceName = "SPOT_SEQ", initialValue = 1, allocationSize = 1)
@Entity
@Data
@Table(name = "spot")
public class Spot {

	@Id
	@Column(name="spot_id")
	@GeneratedValue
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SPOT_SEQ_GENERATOR")
	private Long id;
	
	@Column(name="spot_name")
	private String spotName;
	
	
	@OneToOne
	@JoinColumn(name="main_spot_img_info")
	private UploadFileInfo mainSpotImgInfo;
//	private List<UploadFile> addedSpotImgs;
	
	@Column(columnDefinition = "TEXT")
	private String spotIntroduction;

	
	//	private List<Comment> comments;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User registrant;
	
	
    @OneToMany(mappedBy="visitedSpot")
	private List<VisitorVisitedSpot> visitorVisitedSpots=new ArrayList<>();
	
	
	@Column(name="visited_number")
	private int visitedNumber;
	
	@Column(name="registrant_star_point")
	private float registrantStarPoint;
	
	@Column(name="users_star_point")
	private float usersStarPoint;
	
	
	@OneToMany(mappedBy="spot")
	private List<AddedSpotImg> addedSpotImgs = new ArrayList<>();
		
	
	
}
