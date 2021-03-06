package my.ourShef.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import my.ourShef.domain.bridge.VisitorVisitedSpot;

@SqlResultSetMapping(name = "recentAcquaintanceSpots",
entities = {
		@EntityResult(entityClass = User.class),
		@EntityResult(entityClass = Spot.class)
})
@Entity
@Table(name="user")
@Getter
public class User {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_seq")
	@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", initialValue = 1, allocationSize=1)
	@Id
	@Column(name="user_id")
	private Long id;
	
	@Column(name="account_id")
	private String accountId;
	
	@Column(name="registration_date_time")
	private LocalDateTime registeredTime;
	
	@Setter
	private String password;
	
	@Setter
	@Column(name="nick_name")
	private String nickName;
	
	@Setter
	private String introduction;
	
	@Setter
	@JoinColumn(name="profile_img_info_id")
	@OneToOne
	private UploadFileInfo profileImgInfo;
	
	@Setter
	private float reliability=-1; //default = -1, when set 0~100 (%)
	
	@OneToMany(mappedBy = "registrant")
	private List<Spot> registeredSpots=new ArrayList<>();
	
	@OneToMany(mappedBy="visitor")
	private List<VisitorVisitedSpot> visitorVisitedSpots=new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<UserAcquaintance> UserAcquaintances=new ArrayList<>();
	
	protected User() {
		
	}
	
	public User(String accountId) {
		this.accountId = accountId;
		this.registeredTime = LocalDateTime.now();
	}
	
	
	@Override
	public int hashCode() {
	    return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		User user = (User)obj;
		
		if(this.id == user.id)
		{
			return true;
		}
		
		return false;
	}
	
}
