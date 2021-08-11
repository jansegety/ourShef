package my.ourShef.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.validator.JoinFormValidator;
import my.ourShef.controller.validator.LoginFormValidator;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.file.FilePath;
import my.ourShef.file.FileStore;
import my.ourShef.service.SpotService;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;

@Controller
@RequestMapping("/img")
@Slf4j
@RequiredArgsConstructor
public class ImgController {
	
	@Value("${file.dir.userProfileImg}")
	private String userProfileImgDirPath;
	@Value("${file.dir.spotMainImg}")
	private String spotMainImgDirPath;
	@Value("${file.dir.spotAddedImg}")
	private String spotAddedImgDirPath;
	@Value("${file.file.demoImg}")
	private String demoImgPath;
	
	private final UserService userService;
	private final SpotService spotService;

	/*
	 * return user profile
	 */
	@ResponseBody
	@GetMapping("/loginUserProfile")
	public Resource downloadLoginUserProfile(
			@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId) throws IOException
	{
		User user = userService.findByAccountId(LoginUserAccountId).get();
		UploadFileInfo profileImgInfo = user.getProfileImgInfo();
		String storeProfileImgFileName = profileImgInfo.getStoreFileName();
		File file = new File(userProfileImgDirPath + storeProfileImgFileName);
		byte[] storeProfileImgFileBytes = Files.readAllBytes(file.toPath());
		return new ByteArrayResource(storeProfileImgFileBytes);
	}
	
	/*
	 * return User recent registration spot
	 */
	@ResponseBody
	@GetMapping("/loginUserRecentRegisterationSpot")
	public Resource downloadUserRecentSpot(
			@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId) throws IOException
	{
		
	
		Optional<Spot> findRecentRegisterationSpot = spotService.findRecentRegisterationSpotByUserAccountId(LoginUserAccountId);
		
		//If there is no recently registered spot image
		if(findRecentRegisterationSpot.isEmpty())
		{
			File file = new File(demoImgPath);
			byte[] demoImgFileBytes = Files.readAllBytes(file.toPath());
			return new ByteArrayResource(demoImgFileBytes);
		}
		else
		{
			Spot recentRegisterationSpot = findRecentRegisterationSpot.get();
			UploadFileInfo mainSpotImgInfo = recentRegisterationSpot.getMainSpotImgInfo();
			String storeFileName = mainSpotImgInfo.getStoreFileName();
			File spotMainImgFile = new File(spotMainImgDirPath + storeFileName);
			byte[] spotMainImgFileBytes = Files.readAllBytes(spotMainImgFile.toPath());
			return new ByteArrayResource(spotMainImgFileBytes);
		}
		
	}
	
	@ResponseBody
	@GetMapping("/userProfile/{storeName}")
	public Resource downloadUserProfile(@PathVariable("storeName") String storeFileName ) throws IOException {
		File profileImgFile = new File(userProfileImgDirPath + storeFileName);
		byte[] profileImgFileImgFileBytes = Files.readAllBytes(profileImgFile.toPath());
		return new ByteArrayResource(profileImgFileImgFileBytes);
	}
	
	@ResponseBody
	@GetMapping("/spotMain/{storeName}")
	public Resource downloadSpotMain(@PathVariable("storeName") String storeFileName ) throws IOException {
		File spotMainImgFile = new File(spotMainImgDirPath + storeFileName);
		byte[] spotMainImgFileBytes = Files.readAllBytes(spotMainImgFile.toPath());
		return new ByteArrayResource(spotMainImgFileBytes);
	}
	
	@ResponseBody
	@GetMapping("/spotAdded/{storeName}")
	public Resource downloadSpotAdded(@PathVariable("storeName") String storeFileName ) throws IOException {
		File spotMainImgFile = new File(spotAddedImgDirPath + storeFileName);
		byte[] spotMainImgFileBytes = Files.readAllBytes(spotMainImgFile.toPath());
		return new ByteArrayResource(spotMainImgFileBytes);
	}

	
}
