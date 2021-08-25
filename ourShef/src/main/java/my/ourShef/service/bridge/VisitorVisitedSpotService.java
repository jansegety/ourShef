package my.ourShef.service.bridge;

import java.util.List;
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
@Transactional
@RequiredArgsConstructor
public class VisitorVisitedSpotService {

	private final VisitorVisitedSpotRepository visitorVisitedSpotRepository;
	
	public Long save(VisitorVisitedSpot visitorVisitedSpot) {
		return visitorVisitedSpotRepository.save(visitorVisitedSpot);
	}
	
	public Optional<VisitorVisitedSpot> findById(Long visitorVisitedSpotId){
		return visitorVisitedSpotRepository.findById(visitorVisitedSpotId);
	}
	
	public List<VisitorVisitedSpot> findByUser(User user) {
		
		return visitorVisitedSpotRepository.findByUser(user);
	}
	
	public void delete(VisitorVisitedSpot visitorVisitedSpot){
		visitorVisitedSpotRepository.delete(visitorVisitedSpot);
	}
	
	public Optional<VisitorVisitedSpot> findOneByUserAndSpot(User user, Spot spot){
		
		return visitorVisitedSpotRepository.findOneByUserAndSpot(user, spot);
	}
	
	public boolean isVisitedSpotByTheUser(Spot spot, User user) {
		return visitorVisitedSpotRepository.isVisitedSpotByTheUser(spot, user);
	}
	
}
