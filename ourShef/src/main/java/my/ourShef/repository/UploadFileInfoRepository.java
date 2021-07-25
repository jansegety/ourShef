package my.ourShef.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;

@Slf4j
@Repository
@Component
@RequiredArgsConstructor
public class UploadFileInfoRepository {

	private final EntityManager em;
	

	public String save(UploadFileInfo ufi) {
		em.persist(ufi);
		
		return ufi.getId();
	}
	
}
