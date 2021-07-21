package my.ourShef.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
//@SequenceGenerator(name = "SPOT_SEQ_GENERATOR", sequenceName = "SPOT_SEQ", initialValue = 1, allocationSize = 1)
public class Spot {

	@Id
	@Column(name="SPOT_ID")
	@GeneratedValue
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SPOT_SEQ_GENERATOR")
	private Long id;
	
	private String spotName;
//	private UploadFile mainSpotImg;
//	private List<UploadFile> addedSpotImgs;
	
	@Column(columnDefinition = "TEXT")
	private String spotIntroduction;
//	private List<Comment> comments;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User registrant;
	
//	private List<User> visitors;
	private int visitedNumber;
	private float registrantStarPoint;
	private float usersStarPoint;
	
	
}
