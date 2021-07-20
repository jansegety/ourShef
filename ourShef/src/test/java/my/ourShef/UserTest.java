package my.ourShef;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import my.ourShef.domain.User.User;
import my.ourShef.domain.User.UserRepository;
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
	    	System.out.println("아아아");
	    	tx.rollback();
	    } finally {
	    	tx.commit();
	    	em.close();
	    }
	   
	    EntityManager em2 = emf.createEntityManager();
	    EntityTransaction tx2 = em2.getTransaction();
	    
	    System.out.println("유저 닉네임과 ID : " +userNickName + " " + id);
	    
	    tx2.begin();
	    try {
	    	
	    	User findUser = em2.find(User.class, id);
	    	System.out.println(findUser.getNickName());
	    	Assertions.assertThat(findUser.getNickName()).isEqualTo(userNickName);
	    	tx2.commit();
	    } catch (Exception e) {
	    	System.out.println("2예외발생");
	    	tx2.rollback();
	    } finally {
	    	
	    	em2.close();
	    }
	
	    emf.close();
	}
}
