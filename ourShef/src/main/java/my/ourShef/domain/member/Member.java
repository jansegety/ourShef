package my.ourShef.domain.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import lombok.Data;


@Entity
@Data
public class Member {
	
	@Id @GeneratedValue
	private Long id;
	private String userid;
	private String nickName;
	private String password;

}
