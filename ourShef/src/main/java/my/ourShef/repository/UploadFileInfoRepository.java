package my.ourShef.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;

@Slf4j
@Repository
@Component
@Transactional
@RequiredArgsConstructor
public class UploadFileInfoRepository {

	private final EntityManager em;
	

	public String save(UploadFileInfo ufi) {
		em.persist(ufi);
		
		return ufi.getId();
	}
	
	public void delete(UploadFileInfo uploadFileInfo) {
		em.remove(uploadFileInfo);
	}
	
	public UploadFileInfo findById(String UUID) {
		return em.find(UploadFileInfo.class, UUID);
	}
	
}
