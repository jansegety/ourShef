package my.ourShef.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.User;




@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

	private final EntityManager em;
	
	
	
	public Long save(User user) {
		em.persist(user);
		
		return user.getId();
	}
	
	public Optional<User> findById(Long id) {
		return Optional.ofNullable(em.find(User.class, id));
	}
	
	public Optional<User> findByAccountId(String accountId){
		return findAll().stream().filter(m->m.getAccountId().equals(accountId)).findFirst();
	}
	
	public List<User> findAll() {
		 List<User> resultList = em.createQuery("select m from User m",User.class).getResultList();
		 return resultList;
	}
	
	/*
	 * Prints a list of recent acquaintances and spots registered by the acquaintance.
	 * 
	 * @param loginUser: A user based on whose acquaintance is
	 * @param limit: how many tuples will it take
	 * @param offset: Where to start on the list
	 * 
	 * @return  Object[0] type:User name:acquaintance, Object[1] type:Spot name:registeredSpot
	 */
	@Transactional
	public List<Object[]> getRecentAcquaintanceSpotList(User loginUser, int limit, int offset){
		
		String sql="select ac.*, sp.* from \r\n"
				+ "(select u.* from \r\n"
				+ "(select ua.* from user u join user_acquaintance ua on u.user_id = ua.user_id and u.user_id = 3) ua \r\n"
				+ "join user u on ua.acquaintance_id = u.user_id) ac join spot sp on ac.user_id = sp.registrant_id\r\n"
				+ "order by sp.spot_id desc\r\n"
				+ "limit ? offset ?";
		List<Object[]> resultList = em.createNativeQuery(sql, "recentAcquaintanceSpots").setParameter(1, limit).setParameter(2, offset).getResultList();
		
		return resultList;
		
	}
	
	
	
}
