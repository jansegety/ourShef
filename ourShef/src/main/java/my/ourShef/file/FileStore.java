package my.ourShef.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import my.ourShef.domain.UploadFileInfo;


@Component
public class FileStore {
	
	@Value("${file.dir.userProfileImg}")
	private String userProfileImgDirPath;
	@Value("${file.dir.spotMainImg}")
	private String spotMainImgDirPath;
	@Value("${file.dir.spotAddedImg}")
	private String spotAddedImgDirPath;

	public String getFullPath(String filename, FilePath fileDirPath) {
		
		switch(fileDirPath) {
		case USER_PROFILE_IMG :
			return userProfileImgDirPath + filename;
		case SPOT_MAIN_IMG :
			return spotMainImgDirPath + filename;
		case SPOT_ADDED_IMG :
			return spotAddedImgDirPath + filename;
		default :
			throw new IllegalStateException("설정되지 않은 FilePath입니다.");
		}
		
		
	}

	public List<UploadFileInfo> storeFiles(List<MultipartFile> multipartFiles, FilePath fileDirPath) throws IOException {
		List<UploadFileInfo> storeFileResult = new ArrayList<>();
		
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile, fileDirPath));
			}
		}
		return storeFileResult;
	}

	public UploadFileInfo storeFile(MultipartFile multipartFile, FilePath fileDirPath) throws IOException {
		if (multipartFile.isEmpty()) {
			return null;
		}
		String originalFilename = multipartFile.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);
		multipartFile.transferTo(new File(getFullPath(storeFileName, fileDirPath)));
		return new UploadFileInfo(originalFilename, storeFileName);
	}

	private String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}

	private String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos + 1);
	}
}