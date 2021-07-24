package my.ourShef;



import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.domain.bridge.VisitorVisitedSpot;

@Component
@RequiredArgsConstructor
public class InitDb {
	
private final InitService initService;
	
	@PostConstruct
	public void init(){
		initService.dbInit1();
	}
	
	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {
		
	
		private final EntityManager em;
		
		
		public void dbInit1() {
			
			//user1
			User user1 = new User("USER_A");
			user1.setNickName("AAA");
			user1.setPassword("1111");
			em.persist(user1);
			
			UploadFileInfo profileImgInfo1 = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(profileImgInfo1);
			//프로필 이미지 연결
			user1.setProfileImgInfo(profileImgInfo1); 
			
			//유저 스폿 연결
			Spot spot1 = new Spot(user1, "spotB");
			spot1.setSpotIntroduction("good 맛있어요");
			em.persist(spot1);
			
			
			//user2
			User user2 = new User("USER_B");
			user2.setNickName("BBB");
			user2.setPassword("2222");
			em.persist(user2);
			
			UploadFileInfo profileImgInfo2 = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(profileImgInfo2);
			//프로필 이미지 연결
			user2.setProfileImgInfo(profileImgInfo2); 
			
			//유저 스폿 연결
			Spot spot2 = new Spot(user2, "spotB");
			spot2.setSpotIntroduction("bad 맛없어요");
			em.persist(spot2);
			
			
			
			
			//스폿 방문 유저
			VisitorVisitedSpot user1VisitSpot2 = new VisitorVisitedSpot(user1, spot2);
			em.persist(user1VisitSpot2);
			VisitorVisitedSpot user2VisitSpot1 = new VisitorVisitedSpot(user2, spot1);
			em.persist(user2VisitSpot1);
			
			//comment 생성
			Comment comment1 = new Comment(user1, spot2);
			em.persist(comment1);
			Comment comment2 = new Comment(user2, spot1);
			em.persist(comment2);
			
			//스폿 메인 이미지 업로드
			UploadFileInfo spot1MainImg = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(spot1MainImg);
			spot1.setMainSpotImgInfo(spot1MainImg);
			
			UploadFileInfo spot2MainImg = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(spot2MainImg);
			spot2.setMainSpotImgInfo(spot2MainImg);
			
			//추가 스폿 이미지 업로드
			UploadFileInfo spot1AddedImg1 = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(spot1AddedImg1);
			UploadFileInfo spot1AddedImg2 = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(spot1AddedImg2);
			
			AddedSpotImg spot1addedSpotImg1 = new AddedSpotImg(spot1, spot1AddedImg1);
			em.persist(spot1addedSpotImg1);
			AddedSpotImg spot1addedSpotImg2 = new AddedSpotImg(spot1, spot1AddedImg2);
			em.persist(spot1addedSpotImg2);
			
			UploadFileInfo spot2AddedImg1 = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(spot2AddedImg1);
			UploadFileInfo spot2AddedImg2 = new UploadFileInfo("originalImg.png", "spotTestImg.png");
			em.persist(spot2AddedImg2);
			
			AddedSpotImg spot2addedSpotImg1 = new AddedSpotImg(spot2, spot2AddedImg1);
			em.persist(spot2addedSpotImg1);
			AddedSpotImg spot2addedSpotImg2 = new AddedSpotImg(spot2, spot2AddedImg2);
			em.persist(spot2addedSpotImg2);
			
			
			
			
			
			
		}
	}

}
