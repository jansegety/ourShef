package my.ourShef.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
	
	public void delete(Spot spot) {
		em.remove(spot);
	}
	
	public List<Float> getRegisterationSpotReliabilityListExcludingNotVisited(User user) {
		List<Float> resultList = em.createQuery("SELECT sp.reliability FROM Spot sp WHERE sp.registrant =:user AND sp.reliability != -1", Float.class)
		.setParameter("user", user).getResultList();
		
		return resultList;
	}
	
	public List<Spot> getAllRegisteredSpotsByUser(User user) {
		TypedQuery<Spot> query = em.createQuery("select sp from Spot sp where sp.registrant = :user order by sp.id desc", Spot.class);
		query.setParameter("user", user);
		List<Spot> resultList = query.getResultList();
		
		return resultList;
	}
	
	public List<Spot> getRegisteredSpotsByUserUsingPaging(User user, Long limit, Long offset){
		TypedQuery<Spot> query = em.createQuery("select sp from Spot sp where sp.registrant = :user order by sp.id desc", Spot.class);
		query.setParameter("user", user).setMaxResults(limit.intValue()).setFirstResult(offset.intValue());
		
		return query.getResultList();
	}
	
	public Long getAllRegisteredSpotsNumByUser(User user) {
		Query query = em.createQuery("select count(sp.id) from Spot sp where sp.registrant = :user");
		query.setParameter("user", user);
		List<Object> resultList = query.getResultList();
		
		return (Long)resultList.get(0);
	}
	

}
