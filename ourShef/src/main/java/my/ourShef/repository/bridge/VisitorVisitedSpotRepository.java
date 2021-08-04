package my.ourShef.repository.bridge;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.repository.UserRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VisitorVisitedSpotRepository {

	private final EntityManager em;
	
	public Long save( VisitorVisitedSpot visitorVisitedSpot) {
		em.persist(visitorVisitedSpot);
		return visitorVisitedSpot.getId();
	} 
	
}
