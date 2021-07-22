package my.ourShef.domain.bridge;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

public class VisitorVisitedSpotTest {

	@Test
	void createTest() {
				
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
	    
		EntityManager em = emf.createEntityManager();
		
	    EntityTransaction tx = em.getTransaction();
	    
	    tx.begin();
	    
	    try {
	    	
	    	Spot spotA = new Spot();
	    	spotA.setSpotName("유럽");
	    	Spot spotB = new Spot();
	    	spotB.setSpotName("미국");
	    	
	    	em.persist(spotA);
	    	em.persist(spotB);
	    	
	    	User userA = new User();
	    	userA.setNickName("영희");
	    	User userB = new User();
	    	userB.setNickName("철수");
	    	
	    	em.persist(userA);
	    	em.persist(userB);
	    	
	    	VisitorVisitedSpot vvs1= new VisitorVisitedSpot(userA, spotA);
	    	VisitorVisitedSpot vvs2 = new VisitorVisitedSpot(userA, spotB);
	    	VisitorVisitedSpot vvs3 = new VisitorVisitedSpot(userB, spotB);
	    	
	    	em.persist(vvs1);
	    	em.persist(vvs2);
	    	em.persist(vvs3);
	    	
	    	em.flush();
	    	em.clear();
	    	
	    	Spot findSpot = em.find(Spot.class, spotA.getId());
	    	
	    	assertThat(findSpot.getVisitorVisitedSpots()).allSatisfy(
	    	          visitorVisitedSpot -> assertThat(visitorVisitedSpot.getVisitor()).satisfiesAnyOf(
	    	                      visitor -> {assertThat(visitor.getNickName()).isEqualTo("영희");},
	    	                      visitor -> {assertThat(visitor.getNickName()).isEqualTo("철수");})
	    	         );						
	   
	    	User findUser = em.find(User.class, userA.getId());
	    	
	    	assertThat(findUser.getVisitorVisitedSpots()).allSatisfy(
	    	          visitorVisitedSpot -> assertThat(visitorVisitedSpot.getVisitedSpot()).satisfiesAnyOf(
	    	                      visitedSpot -> {assertThat(visitedSpot.getSpotName()).isEqualTo("유럽");},
	    	                      visitedSpot -> {assertThat(visitedSpot.getSpotName()).isEqualTo("미국");})
	    	         );	
	   
	    	tx.commit();
	    }catch(Exception e) {
	    	
	    	tx.rollback();
	    	throw e;
	    	
	    }finally {
	    	em.close();
	    }
	    
	    emf.close();
	}
}
