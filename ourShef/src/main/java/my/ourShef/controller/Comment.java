package my.ourShef.controller;

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
	User commentUser;
	float starPoint;
	String comment;
	Spot commentedSpot;
	
}
