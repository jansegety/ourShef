package my.ourShef.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Entity
@Table(name = "comment")
@Data
public class Comment {

	@Id @GeneratedValue
	@Column(name="comment_id")
	private Long id;
	//User commentUser;
	@Column(name="star_point")
	float starPoint;
	@Column(columnDefinition = "TEXT")
	String comment;
	//Spot commentedSpot;
	
}
