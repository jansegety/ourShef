package my.ourShef;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import my.ourShef.OurShefApplication;
import my.ourShef.domain.member.Member;
import my.ourShef.domain.member.MemberRepository;


@SpringBootTest
class StartTest {

	@Autowired MemberRepository memberRepository;
	
	@Test
	@Transactional
	@Rollback(false)
	void startTest()
	{
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		
		Member member = new Member();
		member.setUserid("홍길동");
		Long savedId = memberRepository.save(member);
	
		Member findMember = memberRepository.find(savedId);
		
		Assertions.assertThat(findMember.getUserid()).isEqualTo(member.getUserid());
	
	}

}
