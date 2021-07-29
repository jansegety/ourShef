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
	
	
}
