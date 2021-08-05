package my.ourShef.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "comment")
@Getter
public class Comment {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="comment_seq")
	@SequenceGenerator(name = "comment_seq", sequenceName = "comment_seq", initialValue = 1, allocationSize=1)
	@Id
	@Column(name="comment_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="comment_user_id")
	User commentUser;
	
	//10~100
	@Setter
	@Column(name="star_point")
	float starPoint;
	
	@Setter
	@Column(columnDefinition = "TEXT")
	String comment;
	
	@ManyToOne
	@JoinColumn(name="commented_spot_id")
	Spot commentedSpot;
	
	protected Comment() {
		
	}
	
	public Comment(User commentUser, Spot commentedSpot) {
		this.commentUser = commentUser;
		this.commentedSpot = commentedSpot;
	}
	
	
}
