package my.ourShef.domain.bridge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Entity
@Getter
@Table(name = "visitor_visited_spot")
public class VisitorVisitedSpot {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="visitor_visited_spot_seq")
	@SequenceGenerator(name = "visitor_visited_spot_seq", sequenceName = "visitor_visited_spot_seq", initialValue = 1, allocationSize=1)
	@Id
	@Column(name="visitor_visited_spot_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="visitor_id")
	private User visitor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="visited_spot_id")
	private Spot visitedSpot;

	
	protected VisitorVisitedSpot() {
		
	}
	
	public VisitorVisitedSpot(User visitor, Spot visitedSpot) {
		this.visitor = visitor;
		this.visitedSpot = visitedSpot;
	}
	
	
	
	
	
}
