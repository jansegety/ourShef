package my.ourShef.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SpotRepository {

	
	private final EntityManager em;
	
	public Long save(Spot spot) {
		em.persist(spot);
		return spot.getId();
	}
	
}
