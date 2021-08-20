package my.ourShef.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.repository.UploadFileInfoRepository;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UploadFileInfoService {

	private final UploadFileInfoRepository upLoadFileInfoRepository;
	
	
	public void save(UploadFileInfo upLoadFileInfo) {
		upLoadFileInfoRepository.save(upLoadFileInfo);
	}
	
	public void saves(List<UploadFileInfo> upLoadFileInfoList) {
		for(UploadFileInfo upLoadFileInfo : upLoadFileInfoList)
		{
			if(upLoadFileInfo != null)
			upLoadFileInfoRepository.save(upLoadFileInfo);
		}
		
	}
	
	public void delete(UploadFileInfo uploadFileInfo) {
		upLoadFileInfoRepository.delete(uploadFileInfo);
	}
	
	public UploadFileInfo findById(String UUID) {
		return upLoadFileInfoRepository.findById(UUID);
	}
	
	
}
