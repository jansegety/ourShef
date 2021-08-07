package my.ourShef.service.bridge;

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
@RequiredArgsConstructor
public class UserAcquaintanceService {
	
	private final UserAcquaintanceRepository userAcquaintanceRepository;

	@Transactional
	public Long save(UserAcquaintance userAcquaintance) {
		
		return userAcquaintanceRepository.save(userAcquaintance);
	}
	
	/*
	 * Verify that there is no duplicate relationship between user and acquaintance
	 */
	@Transactional
	public boolean isPresentByUserAndAcquaintance(User user, User acquaintance) {

		return userAcquaintanceRepository.isPresentByUserAndAcquaintance(user, acquaintance);
	}
	
}
