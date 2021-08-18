package my.ourShef.repository.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.repository.UserRepository;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class VisitorVisitedSpotRepository {

	private final EntityManager em;
	
	public Long save(VisitorVisitedSpot visitorVisitedSpot) {
		em.persist(visitorVisitedSpot);
		return visitorVisitedSpot.getId();
	} 
	
	public void delete(VisitorVisitedSpot visitorVisitedSpot){
		em.remove(visitorVisitedSpot);
	}
	
	public List<VisitorVisitedSpot> findByUser(User user) {
		
		List<VisitorVisitedSpot> resultList = em.createQuery("select vvs from VisitorVisitedSpot vvs where vvs.visitor = :visitor", VisitorVisitedSpot.class)
		.setParameter("visitor", user).getResultList();
		
		return resultList;
	}
	
	/*
	 * Returns an object with both Spot and User matching	 * 
	 */
	public Optional<VisitorVisitedSpot> findOneByUserAndSpot(User visitor, Spot visitedSpot) {
		Query query = em
				.createQuery("select vvs FROM VisitorVisitedSpot vvs where vvs.visitor = :visitor and vvs.visitedSpot = :visitedSpot");
		
		query.setParameter("visitor", visitor)
				.setParameter("visitedSpot", visitedSpot);		
		
		List<VisitorVisitedSpot> resultList = query.getResultList();
		
		//validation
		if(resultList.size() >= 2) {
			throw new IllegalStateException("VisitorVisitedSpot Entity exists with the same user and spot");
		}
		else if(resultList.size() == 0) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(resultList.get(0));
		
	}
	
	
	public boolean isVisitedSpotByTheUser(Spot spot, User user) {
		
		List<VisitorVisitedSpot> vvsList = findByUser(user);
		
		for(VisitorVisitedSpot vvs : vvsList)
		{
			if(vvs.getVisitedSpot().getId() == spot.getId())
				return true;
		}
		
		return false;	
	}
	
	
}
