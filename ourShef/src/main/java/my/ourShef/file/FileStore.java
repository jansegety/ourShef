package my.ourShef.file;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.UploadFileInfo;

@Slf4j
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
		
		HashSet<FilePath> imgFilePathSet = new HashSet<FilePath>();
		imgFilePathSet.add(FilePath.SPOT_MAIN_IMG);
		imgFilePathSet.add(FilePath.SPOT_ADDED_IMG);
		imgFilePathSet.add(FilePath.USER_PROFILE_IMG);
		
		
		String originalFilename = multipartFile.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);
		
		//If it is an image file
		if(imgFilePathSet.contains(fileDirPath))
		{
		 
			String imgFormat = "jpg";
			
			InputStream inputStream = null;
			
			try {	
				inputStream = multipartFile.getInputStream();
				BufferedImage file = ImageIO.read(inputStream);
				RenderedImage resizedImage = resizeImage(file,fileDirPath);
				
				//write to image file
				ImageIO.write(resizedImage, imgFormat, new File(getFullPath(storeFileName, fileDirPath)));
				
			}catch(IOException ex)
			{
				log.error("File Store Error", ex);
				throw ex;
			}
			finally
			{
				inputStream.close();
			}
		}
		else
		{
			//If it is not an image file, save it immediately
			multipartFile.transferTo(new File(getFullPath(storeFileName, fileDirPath)));
		}	
		
		return new UploadFileInfo(originalFilename, storeFileName);
	}
	
	private RenderedImage resizeImage(Image image, FilePath fileDirPath)
	{
		////Initialize set Values
		int withSetting = 0;
		int heightSettion = 0;
		//W: standard of width, H: standard of height, X: standard value
		String resizeStandard = "X";
		
		
	    ////Values that change according to the set value
		double ratio;
		int newWidth;
		int newHeight;
		
		
		
		//Get original image size
		int originalImageWidth = image.getWidth(null);
		int originalImageHeight = image.getHeight(null);
		
		//Resizing by photo type
		switch (fileDirPath) {
			case USER_PROFILE_IMG :
				
				////Values of setting
				withSetting = 300;
				heightSettion = 300;
				//W: standard of width, H: standard of height, X: standard value
				resizeStandard = "W";
				
				//by width
				if(resizeStandard.equals("W")) {
					ratio = (double)withSetting/(double)originalImageWidth;
					newWidth = (int)(originalImageWidth*ratio);
					newHeight = (int)(originalImageHeight*ratio);	
				}
				//by height
				else if(resizeStandard.equals("H"))
				{
					ratio = (double)heightSettion/(double)originalImageHeight;
					newWidth = (int)(originalImageWidth*ratio);
					newHeight = (int)(originalImageHeight*ratio);	
				}
				//Apply set width and height
				else {
					newWidth = withSetting;
					newHeight = heightSettion;
				}
				break;
				
			case SPOT_MAIN_IMG:
				
			////Values of setting
							withSetting = 600;
							heightSettion = 600;
							//W: standard of width, H: standard of height, X: standard value
							resizeStandard = "W";
							
							//by width
							if(resizeStandard.equals("W")) {
								ratio = (double)withSetting/(double)originalImageWidth;
								newWidth = (int)(originalImageWidth*ratio);
								newHeight = (int)(originalImageHeight*ratio);	
							}
							//by height
							else if(resizeStandard.equals("H"))
							{
								ratio = (double)heightSettion/(double)originalImageHeight;
								newWidth = (int)(originalImageWidth*ratio);
								newHeight = (int)(originalImageHeight*ratio);	
							}
							//Apply set width and height
							else {
								newWidth = withSetting;
								newHeight = heightSettion;
							}
							break;
				
			case SPOT_ADDED_IMG:
			////Values of setting
							withSetting = 400;
							heightSettion = 400;
							//W: standard of width, H: standard of height, X: standard value
							resizeStandard = "W";
							
							//by width
							if(resizeStandard.equals("W")) {
								ratio = (double)withSetting/(double)originalImageWidth;
								newWidth = (int)(originalImageWidth*ratio);
								newHeight = (int)(originalImageHeight*ratio);	
							}
							//by height
							else if(resizeStandard.equals("H"))
							{
								ratio = (double)heightSettion/(double)originalImageHeight;
								newWidth = (int)(originalImageWidth*ratio);
								newHeight = (int)(originalImageHeight*ratio);	
							}
							//Apply set width and height
							else {
								newWidth = withSetting;
								newHeight = heightSettion;
							}
							break;
			default: throw new IllegalArgumentException("이미지 경로가 아닙니다.");				
		}
		
		
		//image resizing
		Image scaledInstance = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH); 
		
		//make RenderedImage 
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = resizedImage.getGraphics();
		g.drawImage(scaledInstance, 0, 0, null);
		g.dispose();
		
		return resizedImage;
	}
	
	
	public void deleteFile(String storeFileName, FilePath filePath) {
		
		 File file = new File(getFullPath(storeFileName, filePath));
		 
		 file.delete();	 
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