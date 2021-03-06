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
import my.ourShef.controller.CommentController;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.form.SpotModificationForm;
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
		//initService.deleteAllImgFile();
		//initDb
		//initService.dbInit();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static public class InitService {

		@Value("${file.dir.userProfileImg}")
		private String userProfileImgDirPath;
		@Value("${file.dir.spotMainImg}")
		private String spotMainImgDirPath;
		@Value("${file.dir.spotAddedImg}")
		private String spotAddedImgDirPath;
		@Value("${file.dir.testImg}")
		private String testImgDirPath;

		private final EntityManager em;
		private final FileStore fileStore;
		private final SpotService spotService;
		private final UploadFileInfoService uploadFileInfoService;
		private final AddedSpotImgService addedSpotImgService;
		private final UserService userService;
		private final CommentController commentController;
		private final CommentService commentService;
		private final VisitorVisitedSpotService visitorVisitedSpotService;
		private final UserAcquaintanceService userAcquaintanceService;

		public ArrayList<JoinForm> makeJoinForm(int formNum) throws IOException {

			ArrayList<JoinForm> joinFormList = new ArrayList<JoinForm>();
			for (int i = 0; i < formNum; i++) {

				JoinForm joinForm = new JoinForm();
				/*
				 * form??? MultipartFile ?????? joinFormProfileImgFile ?????? ??????
				 */
				joinForm.setJoinFormProfileImgFile(makeImgMultipartFile(i));
				joinForm.setJoinFormAccountId("ACCOUNTID_" + Integer.toString(i));
				joinForm.setJoinFormPassword(makePassword(Integer.toString(i)));
				joinForm.setJoinFormConfirmPassword(makePassword(Integer.toString(i)));
				joinForm.setJoinFormNickName("NICKNAME_" + Integer.toString(i));
				joinForm.setJoinFormSelfIntroduction("?????? " + joinForm.getJoinFormNickName() + "?????????.");

				joinFormList.add(joinForm);

			}

			return joinFormList;
		}
		
		public ArrayList<JoinForm> makeJoinFormByRange(int startNum, int endNum) throws IOException {

			ArrayList<JoinForm> joinFormList = new ArrayList<JoinForm>();
			for (int i = startNum; i < endNum; i++) {

				JoinForm joinForm = new JoinForm();
				/*
				 * form??? MultipartFile ?????? joinFormProfileImgFile ?????? ??????
				 */
				joinForm.setJoinFormProfileImgFile(makeImgMultipartFile(i));
				joinForm.setJoinFormAccountId("ACCOUNTID_" + Integer.toString(i));
				joinForm.setJoinFormPassword(makePassword(Integer.toString(i)));
				joinForm.setJoinFormConfirmPassword(makePassword(Integer.toString(i)));
				joinForm.setJoinFormNickName("NICKNAME_" + Integer.toString(i));
				joinForm.setJoinFormSelfIntroduction("?????? " + joinForm.getJoinFormNickName() + "?????????.");

				joinFormList.add(joinForm);

			}

			return joinFormList;
		}

		/*
		 * ????????? ????????? ????????? ???????????? MultipartFile??? ??????????????? ?????? ???????????? i?????? ?????? ????????? ????????? ????????? ??????
		 */
		private CommonsMultipartFile makeImgMultipartFile(int i) throws IOException {

			// MultipartFile??? ????????? ?????? FileItem ??????
			String tempFilePath = userProfileImgDirPath + "temp.jpg";
			File tempFile = new File(tempFilePath);
			DiskFileItem tempFileItem = new DiskFileItem("joinFormProfileImgFile",
					Files.probeContentType(tempFile.toPath()), true, "temp.jpg", 1024, tempFile.getParentFile());

			/*
			 * DiskFileItem??? contents ??????
			 */
			try {
				FileInputStream input = new FileInputStream(getTestImgFilePath(i));
				OutputStream os = tempFileItem.getOutputStream();// ?????? ?????? ?????? ?????? NullPointerException ??????
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
		 * ?????? ?????? ????????? ????????? ????????? ???????????? MultipartFile??? ??????????????? ?????? ???????????? i?????? ?????? ????????? ????????? ????????? ??????
		 */
		private ArrayList<MultipartFile> makeImgMultipartFiles(int i, int num) throws IOException {

			ArrayList<MultipartFile> tempMultipartFileList = new ArrayList<MultipartFile>();
			for (int j = 0; j < num; j++) {
				tempMultipartFileList.add(makeImgMultipartFile(i));
			}
			return tempMultipartFileList;
		}

		/*
		 * ????????? 8?????? ???????????? ?????? ??????????????? 8??? ??????
		 */
		public String makePassword(String word) {
			String password = "";
			for (int i = 0; i < 8; i++)
				password += word;
			return password;
		}

		/*
		 * @return test ????????? ????????? ?????? int ?????? ????????? 5??? ????????? ?????????+1??? ??? ????????? jpg??? ????????? ??????
		 */
		public String getTestImgFilePath(int i) {
			int fileName = i % 5 + 1;
			return testImgDirPath + fileName + ".jpg";
		}

		/*
		 * test??? SporRegisterationFormList??? ???????????????.
		 */
		public ArrayList<SpotRegisterationForm> makeSpotRegisterationForm(int formNum) throws IOException {

			ArrayList<SpotRegisterationForm> spotRegisterationFromList = new ArrayList<SpotRegisterationForm>();
			for (int i = 0; i < formNum; i++) {

				SpotRegisterationForm spotRegisterationForm = new SpotRegisterationForm();
				spotRegisterationForm.setSpotMainImg(makeImgMultipartFile(i));
				spotRegisterationForm.setSpotAddedImgs(makeImgMultipartFiles(i, 6)); // 2?????? ??????????????? ????????? MultipartFile ?????? ???
				Random r=new Random(); //?????? ?????????
				spotRegisterationForm.setSpotName("??????_" + r.nextInt(10000)); //????????? ?????? ?????? ?????? 
				spotRegisterationForm.setSpotIntroduction("????????? " + spotRegisterationForm.getSpotName() + "?????????! ?????? ?????? ?????????!");
				spotRegisterationForm.setStarPoint((r.nextInt(10)+1)*10);// 10~100, 10 20 30 40...90 100

				spotRegisterationFromList.add(spotRegisterationForm);
			}

			return spotRegisterationFromList;

		}
		
		
		public List<SpotModificationForm> makeSpotModificationForm(int formNum) throws IOException{
			

			ArrayList<SpotModificationForm> spotModificationFormList = new ArrayList<SpotModificationForm>();
			for (int i = 0; i < formNum; i++) {

				SpotModificationForm SpotModificationForm = new SpotModificationForm();
				SpotModificationForm.setSpotMainImg(makeImgMultipartFile(i));
				SpotModificationForm.setSpotAddedImgs(makeImgMultipartFiles(i, 6)); // 2?????? ??????????????? ????????? MultipartFile ?????? ???
				Random r=new Random(); //?????? ?????????
				SpotModificationForm.setSpotName("??????_" + r.nextInt(10000)); //????????? ?????? ?????? ?????? 
				SpotModificationForm.setSpotIntroduction("????????? " + SpotModificationForm.getSpotName() + "?????????! ?????? ?????? ?????????!");
				SpotModificationForm.setRegistrantStarPoint((r.nextInt(10)+1)*10);// 10~100, 10 20 30 40...90 100

				spotModificationFormList.add(SpotModificationForm);
			}

			return spotModificationFormList;
			
		}

		/*
		 * ????????? DB??? ????????? ?????? ??????
		 */
		@Transactional
		public ArrayList<User> initUsers(int num) throws Exception {

			ArrayList<User> userList = new ArrayList<User>();
			ArrayList<JoinForm> joinFroms = makeJoinForm(num);

			for (JoinForm joinForm : joinFroms) {
				// ?????? ??????
				User user = new User(joinForm.getJoinFormAccountId());
				user.setPassword(joinForm.getJoinFormPassword());
				user.setNickName(joinForm.getJoinFormNickName());
				user.setIntroduction(joinForm.getJoinFormSelfIntroduction());
				

				// USER_PROFILE_IMG ????????? ????????? ????????? ??????
				UploadFileInfo storeFile = fileStore.storeFile(joinForm.getJoinFormProfileImgFile(),
						FilePath.USER_PROFILE_IMG);
				// UploadFileInfo ?????????
				uploadFileInfoService.save(storeFile);

				// User + ProfileImgInfo ??????
				user.setProfileImgInfo(storeFile);
				userService.join(user);
				userList.add(user);
			}

			return userList;
		}

		/*
		 * ???????????? Spot ???????????? DB??? ????????????. Spot??? ???????????? ???????????? ?????? ????????? ???????????? ????????? ????????????????????????.
		 * 
		 * @param num ???????????? Spot ????????? ???
		 * 
		 * @param registrant Spot?????? ????????? ??????
		 */
		@Transactional
		public ArrayList<Spot> initSpotsByOneUser(int num, User registrant) throws Exception {
			ArrayList<Spot> spotList = new ArrayList<Spot>();

			ArrayList<SpotRegisterationForm> spotRegisterationFormList = makeSpotRegisterationForm(num);
			for (SpotRegisterationForm spotRegisterationForm : spotRegisterationFormList) {

				Spot spot = new Spot(registrant, spotRegisterationForm.getSpotName());
				// ?????????
				spotService.save(spot);

				/* ????????? ?????? */
				UploadFileInfo spotMainImgFileInfo = fileStore.storeFile(spotRegisterationForm.getSpotMainImg(),
						FilePath.SPOT_MAIN_IMG);
				// UploadFileInfo ?????????
				uploadFileInfoService.save(spotMainImgFileInfo);
				// spot + spotMainImgFileInfo ??????
				spot.setMainSpotImgInfo(spotMainImgFileInfo);

				List<UploadFileInfo> spotAddesImgList = fileStore.storeFiles(spotRegisterationForm.getSpotAddedImgs(),
						FilePath.SPOT_ADDED_IMG);
				// UploadFileInfo ?????????
				uploadFileInfoService.saves(spotAddesImgList);

				// Bridge Entity
				// AddedSpotImg ?????? ??? ?????????
				List<AddedSpotImg> addedSpotImgList = addedSpotImgService
						.constructWithUploadFileInfoAndSpot(spotAddesImgList, spot);
				addedSpotImgService.saves(addedSpotImgList);

				/* ????????? ?????? */
				spot.setSpotIntroduction(spotRegisterationForm.getSpotIntroduction()); // Spot ??????
				spot.setRegistrantStarPoint(spotRegisterationForm.getStarPoint()); // Spot??? ?????? ????????? ??????

				spotList.add(spot);
			}

			return spotList;
		}

		/*
		 * DB???????????? ??????????????? ??????????????????.
		 */
		@Transactional
		public void dbInit() throws Exception {

			// All User List
			ArrayList<User> allUserList = new ArrayList<User>();
			// All Spot List
			ArrayList<Spot> allSpotList = new ArrayList<Spot>();

			// initUser
			allUserList = initUsers(10);
			
			//init UserAcquaintance
			initAcquaintance(allUserList);

			// initSpot
			for (User user : allUserList) {
				ArrayList<Spot> spotListByOneUser = initSpotsByOneUser(5, user);
				allSpotList.addAll(spotListByOneUser);
			}
			
			//init Comment And VisitorVisitedSpot
			initCommentAndVisitorVisitedSpot(allUserList, allSpotList);
			
			

		}
		
		/*
		 * Randomly form acquaintance relationships
		 */
		public void initAcquaintance(List<User> userList) {
			
			Random r=new Random(); 
			
			for(User user : userList)
			{
				for(int i=0; i<userList.size() ;i++)
				{
					User acquaintance = userList.get(r.nextInt(userList.size()));
					//If the acquaintance is user, it will pass.
					if(user == acquaintance)
						continue;
					
					List<UserAcquaintance> uaByUserAndAcquaintanceList = userAcquaintanceService.findByUserAndAcquaintance(user, acquaintance);
					List<UserAcquaintance> uaByAcquaintanceAndUserList= userAcquaintanceService.findByUserAndAcquaintance(acquaintance, user);
					//Unless they are acquaintances
					if(uaByUserAndAcquaintanceList.isEmpty()&&uaByAcquaintanceAndUserList.isEmpty())
					{
						UserAcquaintance uaOfUA = new UserAcquaintance(user,acquaintance);
						UserAcquaintance uaOfAU = new UserAcquaintance(acquaintance,user);
						userAcquaintanceService.save(uaOfUA);
						userAcquaintanceService.save(uaOfAU);
					}
					
					
				}
				
			}
			
		}

		
		/*
		 * ?????????????????? ?????? ????????? ????????? ??????????????? ?????? ?????? ???????????? ????????????.
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
					User registrant = visitedSpot.getRegistrant();
					
					//Do not comment unless they are acquaintances.
					if(!userAcquaintanceService.isPresentByUserAndAcquaintance(visitor, registrant))
						continue;
					
					
					
					//comment
					Comment newComment = new Comment(visitor, visitedSpot);
					//persist
					commentService.save(newComment);
					newComment.setComment("?????? ?????? ?????????!" + r.nextInt(10000) + "?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!?????? ?????? ?????????!");
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
					
					//update Spot Reliability
					float spotReliability = 100 - Math.abs(visitedSpot.getRegistrantStarPoint()-visitedSpot.getUsersStarPoint());
					visitedSpot.setReliability(spotReliability);
					
					//Registrant's reliability updates every time a comment is posted
					//Average userStarPoint should be updated.
					updateRegistrantReliability(visitedSpot);
					
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
		
		/*
		 *Registrant's reliability updates every time a comment is posted
		 */
		private void updateRegistrantReliability(Spot spot) {
			commentController.updateRegistrantReliability(spot);
			
		}
		
		
		
		

	}

}
