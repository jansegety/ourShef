package my.ourShef.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import my.ourShef.domain.User;





@Repository
public class UserRepository {

	@PersistenceContext
	private EntityManager em;
	
	public Long save(User user) {
		em.persist(user);
		return user.getId();
	}
	
	public User find(Long id) {
		return em.find(User.class, id);
	}
}
