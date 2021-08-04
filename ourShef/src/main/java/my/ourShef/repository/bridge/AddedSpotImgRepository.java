package my.ourShef.repository.bridge;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.repository.UserRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AddedSpotImgRepository {

	private final EntityManager em;
	
	public Long save(AddedSpotImg addedSpotImg) {
		em.persist(addedSpotImg);
		
		return addedSpotImg.getId();
	}
	
}
