package my.ourShef.service.bridge;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.repository.bridge.AddedSpotImgRepository;
import my.ourShef.repository.bridge.VisitorVisitedSpotRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitorVisitedSpotService {

	private final VisitorVisitedSpotRepository visitorVisitedSpotRepository;
	
	@Transactional
	public Long save(VisitorVisitedSpot visitorVisitedSpot) {
		return visitorVisitedSpotRepository.save(visitorVisitedSpot);
	}
	
	@Transactional
	public Optional<VisitorVisitedSpot> findOneByUserAndSpot(User user, Spot spot){
		
		return visitorVisitedSpotRepository.findOneByUserAndSpot(user, spot);
	}
	
}
