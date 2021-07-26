package my.ourShef.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.User;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

	private final UserRepository userRepository;
	
	/*
	 * @return null이면 로그인 실패
	 */
	public User login(String loginId, String password) {
		
		Optional<User> findUserOptional = userRepository.findByAccountId(loginId);
		try {
			User findUser = findUserOptional.orElseThrow(()->new Exception("존재하지 않는 ID입니다."));
			
			//패스워드가 일치하는 경우 유저 반환
			if(findUser.getPassword().equals(password))
				return findUser;
			else
				return null;
			
		}
		catch(Exception e)
		{
			log.info("존재하지 않는 ID로 로그인 시도");
			return null;
		}
	
	}
	
}
