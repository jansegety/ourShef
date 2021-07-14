package my.ourShef;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import my.ourShef.domain.member.Member;


@SpringBootTest
class OurShefApplicationTests {
	
	@PersistenceContext
	private EntityManager em;

	@Test
	void contextLoads() {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		//EntityManager em = emf.createEntityManager();
		
		Member member = new Member();
		em.persist(member);

		em.close(); 
		//emf.close();
	}

}
