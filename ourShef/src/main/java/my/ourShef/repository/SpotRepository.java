package my.ourShef.repository;

import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SpotRepository {

	
	private final EntityManager em;
	
	public Long save(Spot spot) {
		em.persist(spot);
		return spot.getId();
	}
	
	public Optional<Spot> findById(Long spotId) {
		return Optional.ofNullable(em.find(Spot.class, spotId));
	}
	
	public Long getCountRegisterationSpotNum(User user) {
		Query nativeQuery = em.createNativeQuery("select count(sp.spot_id) from Spot sp where sp.registrant_id = ?");
		nativeQuery.setParameter(1, user.getId());
		return ((BigInteger)nativeQuery.getResultList().get(0)).longValue();
	}
	

}
