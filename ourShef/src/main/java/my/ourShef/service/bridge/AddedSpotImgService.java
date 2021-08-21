package my.ourShef.service.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.repository.bridge.AddedSpotImgRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AddedSpotImgService {

	private final AddedSpotImgRepository addedSpotImgRepository;
	
	
	public Long save(AddedSpotImg addesSpotImg) {
		return addedSpotImgRepository.save(addesSpotImg);
	}
	
	public void saves(List<AddedSpotImg> addesSpotImgList) {
		
		for(AddedSpotImg addesSpotImg : addesSpotImgList) {
			save(addesSpotImg);
		}	
	}
	
	public Optional<AddedSpotImg> findById(Long addedSpotImgId){
		return addedSpotImgRepository.findById(addedSpotImgId);
	}
	
	public List<AddedSpotImg> constructWithUploadFileInfoAndSpot(List<UploadFileInfo> addedSpotImgFileInfoList, Spot spot) {
		
		ArrayList<AddedSpotImg> addedSpotImgList = new ArrayList<AddedSpotImg>();
		for(UploadFileInfo addedSpotFileInfo : addedSpotImgFileInfoList) {
			AddedSpotImg addedSpotImg = new AddedSpotImg(spot, addedSpotFileInfo);
			addedSpotImgList.add(addedSpotImg);
		}
		return addedSpotImgList;
	}
	
	public void delete(AddedSpotImg addedSpotImg) {
		addedSpotImgRepository.delete(addedSpotImg);
	}
	
}