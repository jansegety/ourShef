package my.ourShef.domain;

import java.time.LocalDateTime;
import java.util.List;

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
import my.ourShef.domain.bridge.VisitorVisitedSpot;

@Entity
@Table(name="user_acquaintance")
@Getter
public class UserAcquaintance {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_acquaintance_seq")
	@SequenceGenerator(name = "user_acquaintance_seq", sequenceName = "user_acquaintance_seq", initialValue = 1, allocationSize=1)
	@Id
	@Column(name="user_acquaintance_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "acquaintance_id")
	private User acquaintance;
	
	protected UserAcquaintance() {}
	
	public UserAcquaintance(User user, User acquaintance) {
		this.user = user;
		this.acquaintance = acquaintance;
	}
	
}
