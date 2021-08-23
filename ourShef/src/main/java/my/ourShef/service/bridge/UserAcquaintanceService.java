package my.ourShef.service.bridge;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.User;
import my.ourShef.domain.UserAcquaintance;
import my.ourShef.repository.bridge.AddedSpotImgRepository;
import my.ourShef.repository.bridge.UserAcquaintanceRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserAcquaintanceService {
	
	private final UserAcquaintanceRepository userAcquaintanceRepository;

	
	public Long save(UserAcquaintance userAcquaintance) {
		
		return userAcquaintanceRepository.save(userAcquaintance);
	}
	
	public List<UserAcquaintance> findByUser(User user){
		 
		 return userAcquaintanceRepository.findByUser(user);
	}
	
	public List<UserAcquaintance> findByUserAndAcquaintance(User user, User acquaintance){
	
		 return userAcquaintanceRepository.findByUserAndAcquaintance(user, acquaintance);
	}
	
	public List<User> findAcquaintanceByUser(User User){
		 return userAcquaintanceRepository.findAcquaintanceByUser(User);
	}
	
	
	public void delete(UserAcquaintance userAcquaintance)
	{
		userAcquaintanceRepository.delete(userAcquaintance);
	}
	
	public void deletes(List<UserAcquaintance> userAcquaintances)
	{
		for(UserAcquaintance userAcquaintance : userAcquaintances)
		{
			delete(userAcquaintance);
		}
	}
	
	/*
	 * Verify that there is no duplicate relationship between user and acquaintance
	 */
	@Transactional
	public boolean isPresentByUserAndAcquaintance(User user, User acquaintance) {

		return userAcquaintanceRepository.isPresentByUserAndAcquaintance(user, acquaintance);
	}
	
}
