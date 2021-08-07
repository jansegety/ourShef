package my.ourShef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import lombok.RequiredArgsConstructor;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.form.SpotRegisterationForm;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.domain.UserAcquaintance;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.file.FilePath;
import my.ourShef.file.FileStore;
import my.ourShef.service.CommentService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.AddedSpotImgService;
import my.ourShef.service.bridge.UserAcquaintanceService;
import my.ourShef.service.bridge.VisitorVisitedSpotService;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() throws Exception {
		
		//Delete Files in the ImgDir of the Application
		initService.deleteAllImgFile();
		//initDb
		initService.dbInit();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		@Value("${file.dir.userProfileImg}")
		private String userProfileImgDirPath;
		@Value("${file.dir.spotMainImg}")
		private String spotMainImgDirPath;
		@Value("${file.dir.spotAddedImg}")
		private String spotAddedImgDirPath;

		private final EntityManager em;
		private final FileStore fileStore;
		private final SpotService spotService;
		private final UploadFileInfoService uploadFileInfoService;
		private final AddedSpotImgService addedSpotImgService;
		private final UserService userService;
		private final CommentService commentService;
		private final VisitorVisitedSpotService visitorVisitedSpotService;
		private final UserAcquaintanceService userAcquaintanceService;

		public ArrayList<JoinForm> makeJoinForm(int formNum) throws IOException {

			ArrayList<JoinForm> joinFormList = new ArrayList<JoinForm>();
			for (int i = 0; i < formNum; i++) {

				JoinForm joinForm = new JoinForm();
				/*
				 * form의 MultipartFile 타입 joinFormProfileImgFile 필드 주입
				 */
				joinForm.setJoinFormProfileImgFile(makeImgMultipartFile(i));
				joinForm.setJoinFormAccountId("ACCOUNTID_" + Integer.toString(i));
				joinForm.setJoinFormPassword(makePassword(Integer.toString(i)));
				joinForm.setJoinFormConfirmPassword(makePassword(Integer.toString(i)));
				joinForm.setJoinFormNickName("NICKNAME_" + Integer.toString(i));
				joinForm.setJoinFormSelfIntroduction("저는 " + joinForm.getJoinFormNickName() + "입니다.");

				joinFormList.add(joinForm);

			}

			return joinFormList;
		}

		/*
		 * 이미지 파일이 들어간 테스트용 MultipartFile를 생성해주는 함수 파라미터 i값에 따라 랜덤한 테스트 이미지 선택
		 */
		private CommonsMultipartFile makeImgMultipartFile(int i) throws IOException {

			// MultipartFile를 만들기 위한 FileItem 생성
			String tempFilePath = userProfileImgDirPath + "temp.jpg";
			File tempFile = new File(tempFilePath);
			DiskFileItem tempFileItem = new DiskFileItem("joinFormProfileImgFile",
					Files.probeContentType(tempFile.toPath()), true, "temp.jpg", 1024, tempFile.getParentFile());

			/*
			 * DiskFileItem에 contents 주입
			 */
			try {
				FileInputStream input = new FileInputStream(getTestImgFilePath(i));
				OutputStream os = tempFileItem.getOutputStream();// 이걸 하지 않을 경우 NullPointerException 발생
				IOUtils.copy(input, os);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			CommonsMultipartFile tempMultipartFile = new CommonsMultipartFile(tempFileItem);
			return tempMultipartFile;
		}

		/*
		 * 여러 개의 이미지 파일이 들어간 테스트용 MultipartFile를 생성해주는 함수 파라미터 i값에 따라 랜덤한 테스트 이미지 선택
		 */
		private ArrayList<MultipartFile> makeImgMultipartFiles(int i, int num) throws IOException {

			ArrayList<MultipartFile> tempMultipartFileList = new ArrayList<MultipartFile>();
			for (int j = 0; j < num; j++) {
				tempMultipartFileList.add(makeImgMultipartFile(i));
			}
			return tempMultipartFileList;
		}

		/*
		 * 임의의 8자리 비밀번호 생성 매개변수를 8번 반복
		 */
		public String makePassword(String word) {
			String password = "";
			for (int i = 0; i < 8; i++)
				password += word;
			return password;
		}

		/*
		 * @return test 이미지 경로를 반환 int 값을 받아서 5로 나누어 나머지+1을 한 이름의 jpg를 패스를 반환
		 */
		public String getTestImgFilePath(int i) {
			int fileName = i % 5 + 1;
			return "/OurShefTestImg/" + fileName + ".jpg";
		}

		/*
		 * test용 SporRegisterationFormList를 반환합니다.
		 */
		public ArrayList<SpotRegisterationForm> makeSpotRegisterationForm(int formNum) throws IOException {

			ArrayList<SpotRegisterationForm> spotRegisterationFromList = new ArrayList<SpotRegisterationForm>();
			for (int i = 0; i < formNum; i++) {

				SpotRegisterationForm spotRegisterationForm = new SpotRegisterationForm();
				spotRegisterationForm.setSpotMainImg(makeImgMultipartFile(i));
				spotRegisterationForm.setSpotAddedImgs(makeImgMultipartFiles(i, 6)); // 2번째 파라미터는 만드는 MultipartFile 객체 수
				Random r=new Random(); //난수 생성기
				spotRegisterationForm.setSpotName("스폿_" + r.nextInt(10000)); //랜덤한 숫자 스폿 이름 
				spotRegisterationForm.setSpotIntroduction("여기는 " + spotRegisterationForm.getSpotName() + "입니다! 음식 맛이 좋아요!");
				spotRegisterationForm.setStarPoint((r.nextInt(10)+1)*10);// 10~100, 10 20 30 40...90 100

				spotRegisterationFromList.add(spotRegisterationForm);
			}

			return spotRegisterationFromList;

		}

		/*
		 * 유저를 DB에 초기화 하는 함수
		 */
		@Transactional
		public ArrayList<User> initUsers(int num) throws Exception {

			ArrayList<User> userList = new ArrayList<User>();
			ArrayList<JoinForm> joinFroms = makeJoinForm(num);

			for (JoinForm joinForm : joinFroms) {
				// 성공 로직
				User user = new User(joinForm.getJoinFormAccountId());
				user.setPassword(joinForm.getJoinFormPassword());
				user.setNickName(joinForm.getJoinFormNickName());
				user.setIntroduction(joinForm.getJoinFormSelfIntroduction());
				

				// USER_PROFILE_IMG 경로에 프로필 이미지 저장
				UploadFileInfo storeFile = fileStore.storeFile(joinForm.getJoinFormProfileImgFile(),
						FilePath.USER_PROFILE_IMG);
				// UploadFileInfo 영속화
				uploadFileInfoService.save(storeFile);

				// User + ProfileImgInfo 연결
				user.setProfileImgInfo(storeFile);
				userService.join(user);
				userList.add(user);
			}

			return userList;
		}

		/*
		 * 테스트용 Spot 데이터를 DB에 넣어준다. Spot를 등록하기 위해서는 유저 정보가 필요하기 때문에 유저정보를받는다.
		 * 
		 * @param num 생성되는 Spot 객체의 수
		 * 
		 * @param registrant Spot들과 맵핑된 유저
		 */
		@Transactional
		public ArrayList<Spot> initSpotsByOneUser(int num, User registrant) throws IOException {
			ArrayList<Spot> spotList = new ArrayList<Spot>();

			ArrayList<SpotRegisterationForm> spotRegisterationFormList = makeSpotRegisterationForm(num);
			for (SpotRegisterationForm spotRegisterationForm : spotRegisterationFormList) {

				Spot spot = new Spot(registrant, spotRegisterationForm.getSpotName());
				// 영속화
				spotService.save(spot);

				/* 이미지 처리 */
				UploadFileInfo spotMainImgFileInfo = fileStore.storeFile(spotRegisterationForm.getSpotMainImg(),
						FilePath.SPOT_MAIN_IMG);
				// UploadFileInfo 영속화
				uploadFileInfoService.save(spotMainImgFileInfo);
				// spot + spotMainImgFileInfo 연결
				spot.setMainSpotImgInfo(spotMainImgFileInfo);

				List<UploadFileInfo> spotAddesImgList = fileStore.storeFiles(spotRegisterationForm.getSpotAddedImgs(),
						FilePath.SPOT_ADDED_IMG);
				// UploadFileInfo 영속화
				uploadFileInfoService.saves(spotAddesImgList);

				// Bridge Entity
				// AddedSpotImg 생성 및 영속화
				List<AddedSpotImg> addedSpotImgList = addedSpotImgService
						.constructWithUploadFileInfoAndSpot(spotAddesImgList, spot);
				addedSpotImgService.saves(addedSpotImgList);

				/* 나머지 처리 */
				spot.setSpotIntroduction(spotRegisterationForm.getSpotIntroduction()); // Spot 소개
				spot.setRegistrantStarPoint(spotRegisterationForm.getStarPoint()); // Spot에 대한 등록자 별점

				spotList.add(spot);
			}

			return spotList;
		}

		/*
		 * DB데이터를 복합적으로 초기화합니다.
		 */
		@Transactional
		public void dbInit() throws Exception {

			// All User List
			ArrayList<User> allUserList = new ArrayList<User>();
			// All Spot List
			ArrayList<Spot> allSpotList = new ArrayList<Spot>();

			// initUser
			allUserList = initUsers(10);

			// initSpot
			for (User user : allUserList) {
				ArrayList<Spot> spotListByOneUser = initSpotsByOneUser(5, user);
				allSpotList.addAll(spotListByOneUser);
			}
			
			//init Comment And VisitorVisitedSpot
			initCommentAndVisitorVisitedSpot(allUserList, allSpotList);
			
			//init UserAcquantance
			initUserAcquaintance(allUserList);

		}

		
		/*
		 * 어플리케이션 유저 업로드 이미지 디렉토리에 있는 모든 이미지를 지웁니다.
		 */
		public void deleteAllImgFile() {

			ArrayList<String> pathList = new ArrayList<String>();
			pathList.add(userProfileImgDirPath);
			pathList.add(spotMainImgDirPath);
			pathList.add(spotAddedImgDirPath);

			for (String path : pathList) {
				File userImgDir = new File(path);

				if (userImgDir.exists()) {
					File[] fileArrayToBeDeleted = userImgDir.listFiles();

					for (int i = 0; i < fileArrayToBeDeleted.length; i++) {
						fileArrayToBeDeleted[i].delete();
					}

				}

			}

		}
		
		/*
		 * Assuming that the user comments on the spot, the comment data is initialized.
		 * And the VisitedVisitorSpot Entity mapped with it is also initialized.
		 * This is because the user who commented becomes the user who visited.
		 * Even if the comment is deleted, it is registered as a person who visited.
		 * Comments can be duplicated, and registrants can also add.
		 * A random number of comments for each user are initialized in a random spot.
		 * 
		 * @return A Map object that maps the user and the spot the user left a comment on is output.
		 */
		@Transactional
		public Map<User, List<Spot>> initCommentAndVisitorVisitedSpot(List<User> userList, List<Spot> spotList){
			
			
			HashMap<User, List<Spot>> userAndCommentedSpotMap = new HashMap<User, List<Spot>>();
			
			int spotListSize = spotList.size();
			Random r=new Random(); 
			
			
			for(User visitor : userList) {
				
				List<Spot> commentedSpotList = new ArrayList<Spot>();
				
				//30 number of comments per user
				for(int i = 0; i < 30; i++) {
					
					//Pick a random spot.
					Spot visitedSpot = spotList.get(r.nextInt(spotListSize)); 
					
					//comment
					Comment newComment = new Comment(visitor, visitedSpot);
					//persist
					commentService.save(newComment);
					newComment.setComment("정말 맛이 좋아요!" + r.nextInt(10000));
					newComment.setStarPoint((r.nextInt(10)+1)*10); //10~100
				
					//Average star point rating
					float allStarPoint = 0;
					if(visitedSpot.getUsersStarPoint() == -1)
					{
						allStarPoint = newComment.getStarPoint();
					}
					else
					{
						allStarPoint = (visitedSpot.getUsersStarPoint() * visitedSpot.getVisits())+newComment.getStarPoint();
					}
					
					float averageStarPoint = allStarPoint/(visitedSpot.getVisits()+1);
					
					visitedSpot.setUsersStarPoint(averageStarPoint);
					
					//visits + 1
					visitedSpot.setVisits(visitedSpot.getVisits() + 1);
					
					//updated as a visitor
					//before update, validation
					Optional<VisitorVisitedSpot> OptionalVisitorVisitedSpot = 
							visitorVisitedSpotService.findOneByUserAndSpot(visitor, visitedSpot);
					
					//If the spot has been already visited by the user, do not update it.
					if(OptionalVisitorVisitedSpot.isPresent())
					{
						continue;
					}
					else
					{	
						//persist
						visitorVisitedSpotService.save(new VisitorVisitedSpot(visitor, visitedSpot));
					}
					
					commentedSpotList.add(visitedSpot);	
				}
				
				userAndCommentedSpotMap.put(visitor, commentedSpotList);
								
			}
			
			return userAndCommentedSpotMap;
		}
		
	
		
		/*
		 * init UserAcquaintance
		 */
		private void initUserAcquaintance(List<User> userList){
			
			Random r=new Random(); //random number generator
			
			//loop per User
			for(int i = 0; i < userList.size(); i++)
			{
				for(int j = 0; j < userList.size(); j++)
				{
					//The same user cannot become acquaintances.
					if(i == j)
						continue;
					
					int isAquaintance = r.nextInt(2); //0~1, 0 is no, 1 is yes 
					
					//Skip without becoming an acquaintance
					if(isAquaintance == 0)
						continue;
					
					////be acquaintance logic////
					
					//Skip Duplication
					if(userAcquaintanceService.isPresentByUserAndAcquaintance(userList.get(i), userList.get(j)))
					{
						continue;
					}
					UserAcquaintance case1 = new UserAcquaintance(userList.get(i), userList.get(j));
					UserAcquaintance case2 = new UserAcquaintance(userList.get(j), userList.get(i));
					//UserAcquaintance persist in both case
					userAcquaintanceService.save(case1);
					userAcquaintanceService.save(case2);
					
			
				}
				
			}
			
			
			
		}
		
		
		

	}

}
