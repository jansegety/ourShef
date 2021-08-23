package my.ourShef.repository.bridge;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.User;
import my.ourShef.domain.UserAcquaintance;


@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class UserAcquaintanceRepository {

private final EntityManager em;
	
	public Long save(UserAcquaintance userAcquaintance) {
		em.persist(userAcquaintance);
		
		return userAcquaintance.getId();
	}
	
	public List<UserAcquaintance> findByUser(User user){
		 List<UserAcquaintance> resultList = 
				 em.createQuery("SELECT ua FROM UserAcquaintance ua WHERE ua.user =:user",UserAcquaintance.class)
				 .setParameter("user", user)
				 .getResultList();
		 
		 return resultList;
	}
	
	public List<UserAcquaintance> findByUserAndAcquaintance(User user, User acquaintance){
		 List<UserAcquaintance> resultList = 
				 em.createQuery("SELECT ua FROM UserAcquaintance ua WHERE ua.user =:user AND ua.acquaintance =:acquaintance",UserAcquaintance.class)
				 .setParameter("user", user)
				 .setParameter("acquaintance", acquaintance)
				 .getResultList();
		 
		 return resultList;
	}
	
	public List<User> findAcquaintanceByUser(User user){
		 List<User> resultList = 
				 em.createQuery("SELECT ac FROM UserAcquaintance ua JOIN ua.acquaintance ac ON ua.user =:user ORDER BY ac.nickName ASC",User.class)
				 .setParameter("user", user)
				 .getResultList();
		 
		 return resultList;
	}
	
	public void delete(UserAcquaintance userAcquaintance)
	{
		em.remove(userAcquaintance);
	}
	
	/*
	 * Check whether duplicate values with the same user and aquaintance values are stored.
	 */
	public boolean isPresentByUserAndAcquaintance(User user, User acquaintance) {
		
		TypedQuery<UserAcquaintance> query = em.createQuery("select ua from UserAcquaintance ua where ua.user = :user and ua.acquaintance = :acquaintance",UserAcquaintance.class);
		query.setParameter("user", user)
		.setParameter("acquaintance", acquaintance);
		
		return !query.getResultList().isEmpty();
	}
	
}
