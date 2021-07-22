package my.ourShef.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;


public class UserTest {
	
//	int i = 0;
	
	@BeforeEach
	void testCount() {
//		i++;
	}
	
	@AfterEach
	void textCountResult() {
//		System.out.println("테스트 총 실행 횟수 = " + i);
	}

	@Test
	@Rollback(false)
	void startTest() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Long id = null;
		String userNickName = null;

		try {
//			System.out.println("@@@@@@@@@@@@@@@@UserSpotMappingTest 시작@@@@@@@@@@@@@@");
			User user = new User();
			user.setNickName("홍길동");
			userNickName = user.getNickName();
			em.persist(user);
			id = user.getId();
//			System.out.println("@@@@@@@@@@@@@@@@UserSpotMappingTest 끝@@@@@@@@@@@@@@");
		} catch (Exception e) {
			System.out.println("예외발생1");
			tx.rollback();
		} finally {
			tx.commit();
			// em.close();
		}

		EntityManager em2 = emf.createEntityManager();
		EntityTransaction tx2 = em2.getTransaction();

		System.out.println("유저 닉네임과 ID : " + userNickName + " " + id);

		tx2.begin();
		try {

			User findUser = em.find(User.class, id);
			System.out.println(findUser.getNickName());
			assertThat(findUser.getNickName()).isEqualTo(userNickName);

		} catch (Exception e) {
			System.out.println("예외발생2");
			tx2.rollback();
		} finally {
			tx2.commit();
			em2.close();
		}

		emf.close();
	}

	@Test
	@Rollback(false)
	@Transactional
	void UserSpotMappingTest() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		try {
//			System.out.println("@@@@@@@@@@@@@@@@UserSpotMappingTest 시작@@@@@@@@@@@@@@");
			User user = new User();
			user.setNickName("철수");
			em.persist(user);

			Spot spot = new Spot(user, "하와이");


			em.persist(spot);

			em.flush();
			em.clear();

			User findUser = em.find(User.class, user.getId());
			List<Spot> spots = findUser.getRegisteredSpots();

//			System.out.println("닉네임 = " + findUser.getNickName());

			for (Spot s : spots) {
				System.out.println("s = " + s.getSpotName());
			}
			tx.commit();
//			System.out.println("@@@@@@@@@@@@@@@@UserSpotMappingTest 끝@@@@@@@@@@@@@@");
		} catch (Exception e) {
			System.out.println("애러 발생3 :" + e.getMessage());
			tx.rollback();
		} finally {
			em.clear();
		}

		emf.close();
	}

	@Test
	@Rollback(false)
	@Transactional
	void UserProfileImgTest() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		try {
//			System.out.println("@@@@@@@@@@@@@@@@UserProfileImgTest 시작@@@@@@@@@@@@@@");
			User user = new User();
			user.setNickName("유미");
			em.persist(user);

			UploadFileInfo ufi = new UploadFileInfo("소닉", "마리오");
			
			em.persist(ufi);
			
			user.setProfileImgInfo(ufi);
			
			em.flush();
			em.clear();
			
			User findUser = em.find(User.class, user.getId());
			
			assertThat(findUser.getProfileImgInfo().getStoreFileName()).isEqualTo("마리오");
			assertThat(findUser.getProfileImgInfo().getUploadFileName()).isEqualTo("소닉");
			
			tx.commit();
//			System.out.println("@@@@@@@@@@@@@@@@UserProfileImgTest 끝@@@@@@@@@@@@@@");
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			em.clear();
		}
		emf.close();

	}

}
