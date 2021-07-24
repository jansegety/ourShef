package my.ourShef.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
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

}
