package my.ourShef.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Slf4j
@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
	private UserRepository ur;

	@Test
	void findAllTest() {
		List<User> findUsers = ur.findAll();
	
		for(User user : findUsers)
		{
			log.info("user nuickName = {}",user.getNickName());
		}
	}
	
	@Test
	void getRecentAcquaintanceSpotListTest() {
		
		Optional<User> findById = ur.findById(1L);
		
		List<Object[]> recentAcquaintanceSpotList = ur.getRecentAcquaintanceSpotList(findById.get(), 10, 0);
		
		for(Object[] recentAcquaintanceSpot : recentAcquaintanceSpotList) {
			
			User acquaintance = (User)recentAcquaintanceSpot[0];
			Spot recentSpot = (Spot)recentAcquaintanceSpot[1];
			
			System.out.println();
			System.out.print("Acquaintance.id : " + acquaintance.getId());
			System.out.print("  Acquaintance.nickname : " + acquaintance.getNickName());
			System.out.print("  Spot.id : " + recentSpot.getId());
			System.out.println("  Spot.name : " + recentSpot.getSpotName());
			
		}
		
	}
	

}
