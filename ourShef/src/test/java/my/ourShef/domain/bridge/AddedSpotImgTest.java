package my.ourShef.domain.bridge;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;

public class AddedSpotImgTest {

	@Test
	void createTest() throws Exception {
				
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
	    
		EntityManager em = emf.createEntityManager();
		
	    EntityTransaction tx = em.getTransaction();
	    
	    tx.begin();
	    
	    try {
	    	User user = new User("홍길동");
	    	em.persist(user);
	    	Spot spot1 = new Spot(user, "맛있는 식당");
	    	em.persist(spot1);
	    	UploadFileInfo ufi1 = new UploadFileInfo("업로드 파일 A", "저장된 파일 A");
	    	em.persist(ufi1);
	    	AddedSpotImg asi1 = new AddedSpotImg(spot1, ufi1);
	    	em.persist(asi1);
	    	
	    
	    	UploadFileInfo ufi2 = new UploadFileInfo("업로드 파일 B", "저장된 파일 B");
	    	em.persist(ufi2);
	    	AddedSpotImg asi2 = new AddedSpotImg(spot1, ufi2);
	    	em.persist(asi2);
	    	
	    	
	    	em.flush();
	    	em.clear();
	    	
	    	Spot findSpot1 = em.find(Spot.class, spot1.getId());
	
	    	List<AddedSpotImg> addedSpotImgs = findSpot1.getAddedSpotImgs();
	    	
	    	AddedSpotImg addedSpotImg1 = addedSpotImgs.get(0);
	    	AddedSpotImg addedSpotImg2 = addedSpotImgs.get(1);
	    	
	    	if (!addedSpotImg1.getUploadFileInfo().getStoreFileName().equals("저장된 파일 A"))
	    		throw new Exception("AddedSpotImg Class createTest error");
	    	
	    	if (!addedSpotImg2.getUploadFileInfo().getStoreFileName().equals("저장된 파일 B"))
	    		throw new Exception("AddedSpotImg Class createTest error");
	    	
	    	
	    	
	    }catch(Exception e) {
	    	
	    	tx.rollback();
	    	throw e;
	    	
	    }finally {
	    	em.close();
	    }
	    
	    emf.close();
	}
	
}
