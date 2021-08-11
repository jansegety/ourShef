package my.ourShef.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.repository.SpotRepository;
import my.ourShef.repository.UploadFileInfoRepository;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SpotService {

	private final SpotRepository spotRepository;
	private final UserRepository userRepository;
	
	public void save(Spot spot) {
		spotRepository.save(spot);
	}
	
	public Optional<Spot> findById(Long spotId) {
		return spotRepository.findById(spotId);
	}
	
	public Optional<Spot> findRecentRegisterationSpotByUserAccountId(String userAccountId) {
		User findUser = userRepository.findByAccountId(userAccountId).get();
		List<Spot> registeredSpots = findUser.getRegisteredSpots();
		if(registeredSpots.size() == 0) {
			return Optional.empty();
		}
		else
		{   //Return the most recently registered place
			return Optional.of(registeredSpots.get(registeredSpots.size()-1));
		}
			
	}
	
	public Long getCountRegisterationSpotNum(User user) {
		return spotRepository.getCountRegisterationSpotNum(user);
	}
	
}
