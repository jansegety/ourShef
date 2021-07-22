package my.ourShef.domain.bridge;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;

public class UploadFileInfoTest {

	@Test
	void createTest() {
				
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
	    
		EntityManager em = emf.createEntityManager();
		
	    EntityTransaction tx = em.getTransaction();
	    
	    tx.begin();
	    
	    try {
	    	UploadFileInfo fi = new UploadFileInfo("업로드 파일이름", "저장 파일이름");
	    	em.persist(fi);
	    	
	    	em.flush();
	    	em.clear();
	    	
	    	UploadFileInfo findFileInfo = em.find(UploadFileInfo.class, fi.getId());
	    	
	    	Assertions.assertThat(findFileInfo.getStoreFileName()).isEqualTo("저장 파일이름");
	    	
	    }catch(Exception e) {
	    	
	    	tx.rollback();
	    	throw e;
	    	
	    }finally {
	    	em.close();
	    }
	    
	    emf.close();
	}
	
	@Test
	void mainSpotImgTest() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
	    
		EntityManager em = emf.createEntityManager();
		
	    EntityTransaction tx = em.getTransaction();
	    
	    tx.begin();
	    
	    try {
	    	UploadFileInfo fi = new UploadFileInfo("업로드 메인 스폿 이미지", "저장된 메인 스폿 이미지");
	    	em.persist(fi);
	    	
	    	Spot spot = new Spot();
	    	em.persist(spot);
	    	
	    	spot.setMainSpotImgInfo(fi);
	    	
	    	em.flush();
	    	em.clear();
	    	
	    	Spot findSpot = em.find(Spot.class, spot.getId());
	    	
	    	Assertions.assertThat(findSpot.getMainSpotImgInfo().getStoreFileName()).isEqualTo("저장된 메인 스폿 이미지");
	    	Assertions.assertThat(findSpot.getMainSpotImgInfo().getUploadFileName()).isEqualTo("업로드 메인 스폿 이미지");
	    	
	    }catch(Exception e) {
	    	
	    	tx.rollback();
	    	throw e;   	
	    }finally {
	    	em.close();
	    }
	    
	    emf.close();
	}
	
}
