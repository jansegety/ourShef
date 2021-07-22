package my.ourShef.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "comment")
@Getter
public class Comment {

	@Id @GeneratedValue
	@Column(name="comment_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="comment_user_id")
	User commentUser;
	
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
