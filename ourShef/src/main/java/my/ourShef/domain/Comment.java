package my.ourShef.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Entity
@Data
public class Comment {

	@Id @GeneratedValue
	private Long id;
	//User commentUser;
	float starPoint;
	@Column(columnDefinition = "TEXT")
	String comment;
	//Spot commentedSpot;
	
}
