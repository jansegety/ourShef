package my.ourShef.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

public class CommentTest {

	@Test
	void createTest() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		try {
			
			User user = new User();
			user.setNickName("장산");
			Spot spot = new Spot();
			spot.setSpotName("호주");
			
			Comment comment1 = new Comment(user, spot);
			Comment comment2 = new Comment(user, spot);
			
			em.persist(user);
			em.persist(spot);
			em.persist(comment1);
			em.persist(comment2);
			
			em.flush();
			em.clear();
			
			
			
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.clear();
		}

		emf.close();
	}
	
}
