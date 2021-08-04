package my.ourShef.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.repository.SpotRepository;
import my.ourShef.repository.UploadFileInfoRepository;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpotService {

	private final SpotRepository spotRepository;
	
	public void save(Spot spot) {
		spotRepository.save(spot);
	}
	
}
