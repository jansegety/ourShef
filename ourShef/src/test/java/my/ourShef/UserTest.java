package my.ourShef;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.domain.member.Member;
import my.ourShef.domain.member.MemberRepository;


public class UserTest {
	
	
  
	@Test
	void startTest()
	{
		  EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		    
		  EntityManager em = emf.createEntityManager();
		
	    EntityTransaction tx = em.getTransaction();
	    tx.begin();
	    Long id=null;
	    String userNickName=null;
	    
	    try {
	    	
	    	User user = new User();
			user.setNickName("홍길동");
			userNickName = user.getNickName();
			em.persist(user);
			id = user.getId();
	    } catch (Exception e) {
	    	System.out.println("예외발생1");
	    	tx.rollback();
	    } finally {
	    	tx.commit();
	    	//em.close();
	    }
	   
	    EntityManager em2 = emf.createEntityManager();
	    EntityTransaction tx2 = em2.getTransaction();
	
	    System.out.println("유저 닉네임과 ID : " +userNickName + " " + id);
	    
	    tx2.begin();
	    try {
	    	
	    	User findUser = em.find(User.class, id);
	    	System.out.println(findUser.getNickName());
	    	Assertions.assertThat(findUser.getNickName()).isEqualTo(userNickName);
	    	
	    } catch (Exception e) {
	    	System.out.println("예외발생2");
	    	tx2.rollback();
	    } finally {
	    	tx2.commit();
	    	em2.close();
	    }
	
	    emf.close();
	}
	
	
	@Test
	void UserSpotMappingTest()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		try {
			User user = new User();
			user.setNickName("철수");
			em.persist(user);
			
			Spot spot = new Spot();
			spot.setSpotName("하와이");
			
			user.addRegisteredSpots(spot);
			
			em.persist(spot);
			
//			em.flush();
//			em.clear();
			
			User findUser = em.find(User.class, user.getId());
			List<Spot> spots = findUser.getRegisteredSpots();
			
//			System.out.println("닉네임 = " + findUser.getNickName());
			
			for (Spot s : spots) {
				System.out.println("s = " + s.getSpotName());
			}
			tx.commit();
		}catch(Exception e) {
			System.out.println("애러 발생3 :" + e.getMessage());
			tx.rollback();
		}finally {
			em.clear();
		}
		
		emf.close();
	}
	
	
	
}
