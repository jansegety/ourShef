package my.ourShef.repository.bridge;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.repository.UserRepository;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class AddedSpotImgRepository {

	private final EntityManager em;
	
	public Long save(AddedSpotImg addedSpotImg) {
		em.persist(addedSpotImg);
		
		return addedSpotImg.getId();
	}
	
	public Optional<AddedSpotImg> findById(Long addedSpotImgId){
		return Optional.ofNullable(em.find(AddedSpotImg.class, addedSpotImgId));
	}
	
	public void delete(AddedSpotImg addedSpotImg) {
		em.remove(addedSpotImg);
	}
	
}
