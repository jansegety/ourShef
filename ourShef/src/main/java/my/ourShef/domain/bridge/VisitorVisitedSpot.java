package my.ourShef.domain.bridge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Entity
@Getter
@Table(name = "visitor_visited_spot")
public class VisitorVisitedSpot {

	@Id @GeneratedValue
	@Column(name="visitor_visited_spot_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="visitor_id")
	private User visitor;
	
	@ManyToOne
	@JoinColumn(name="visited_spot_id")
	private Spot visitedSpot;

	
	protected VisitorVisitedSpot() {
		
	}
	
	public VisitorVisitedSpot(User visitor, Spot visitedSpot) {
		this.visitor = visitor;
		this.visitedSpot = visitedSpot;
	}
	
	
	
	
	
}
