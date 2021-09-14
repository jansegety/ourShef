package my.ourShef.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import my.ourShef.InitDb;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.form.SpotModificationForm;
import my.ourShef.controller.form.SpotRegisterationForm;
import my.ourShef.controller.form.WithdrawForm;
import my.ourShef.controller.validator.SpotModificationFormValidator;
import my.ourShef.controller.validator.SpotRegisterationFormValidator;
import my.ourShef.domain.Comment;
import my.ourShef.domain.RelationshipRequest;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.domain.UserAcquaintance;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.domain.bridge.VisitorVisitedSpot;
import my.ourShef.file.FileStore;
import my.ourShef.service.CommentService;
import my.ourShef.service.RelationshipRequestService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.AddedSpotImgService;
import my.ourShef.service.bridge.UserAcquaintanceService;
import my.ourShef.service.bridge.VisitorVisitedSpotService;

@SpringBootTest
@RequiredArgsConstructor
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)

class SpotControllerTest {

	@Autowired
	private LoginController loginController;
	@Autowired
	private SpotController spotController;
	@Autowired
	private SpotService spotService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private InitDb.InitService initService;
	@Autowired
	private UserService userService;
	@Autowired
	private RelationshipRequestService relationshipRequestService;
	@Autowired
	private UserAcquaintanceService userAcquaintanceService;
	@Autowired
	private UploadFileInfoService uploadFileInfoService;
	@Autowired
	private AddedSpotImgService addedSpotImgService;
	@Autowired
	private VisitorVisitedSpotService visitorVisitedSpotService;
	@Autowired
	private EntityManager em;


	@Rollback(false)
	@Order(1)
	@Test
	@Transactional
	@DisplayName("장소 등록 테스트")
	void spotRegisterationTest(@Mock BindingResult bindingResult, @Mock Model model) throws Exception {
		
		int check = 0;
		
		ArrayList<JoinForm> joinFormList = new ArrayList<>();
		ArrayList<User> userList = new ArrayList<>();
		ArrayList<SpotRegisterationForm> spotRegisterationFormList = new ArrayList<>();
		when(bindingResult.hasErrors()).thenReturn(false);//Mock of boolan Return type default is falsue


		// ACCOUNTID_100~ACCOUNTID_109
		joinFormList = initService.makeJoinFormByRange(100, 110);

		for (JoinForm joinForm : joinFormList) {
			loginController.create(joinForm, bindingResult);
			
			em.flush();
			
			Optional<User> userOptioanl = userService.findByAccountId(joinForm.getJoinFormAccountId());
																
			assertNotNull(userOptioanl.get());
			userList.add(userOptioanl.get());
		}
		
		//registeration Spot
		for(User user : userList)
		{
			spotRegisterationFormList = initService.makeSpotRegisterationForm(5);
	
			for(SpotRegisterationForm spotRegisterationForm : spotRegisterationFormList) {
				
				spotController.registerSpot(user.getAccountId(), spotRegisterationForm, bindingResult);
			
				
			}
			
			em.flush();
			em.clear();
			 
			//validation spot registeration 
			Optional<User> findUserOptioanl = userService.findById(user.getId());
			User findUser = findUserOptioanl.get();
			List<Spot> registeredSpots = findUser.getRegisteredSpots();
			
			Assert.notEmpty(registeredSpots, "registeredSpots는 원소를 가져야 합니다.");
			for(int i=0; i < spotRegisterationFormList.size(); i++) {
				assertEquals(registeredSpots.get(i).getSpotName(),spotRegisterationFormList.get(i).getSpotName());
			}
			
			
			
		}
		
	
		
		
		
		

	}
	
	
	@Order(2)
	@Test
	@Transactional
	@DisplayName("장소 수정 테스트")
	void testModifySpot(@Mock BindingResult bindingResult, @Mock Model model, @Mock RedirectAttributes redirectAttributes) throws Exception {
		
		User beforeUser = userService.findByAccountId("ACCOUNTID_100").get();
		Spot beforeSpot = beforeUser.getRegisteredSpots().get(0);
		UploadFileInfo beforeMainSpotImgInfo = beforeSpot.getMainSpotImgInfo();
		List<AddedSpotImg> beforeAddedSpotImgs = beforeSpot.getAddedSpotImgs();
		ArrayList<UploadFileInfo> beforeAddedSpotImgInfos = new ArrayList<UploadFileInfo>();
		
		
		
		
		for(int i=0; i < beforeAddedSpotImgs.size(); i++)
		{
			beforeAddedSpotImgInfos.add(beforeAddedSpotImgs.get(i).getUploadFileInfo());
		}
		
		
		//spot Modification
		SpotModificationForm spotModificationForm = initService.makeSpotModificationForm(1).get(0);
		spotController.modifySpot(beforeUser.getAccountId(), model, spotModificationForm, bindingResult, beforeSpot.getId(), redirectAttributes);
		
		em.flush();
		em.clear();
		
		//after Modification
		
		//refresh Persistence context
		User afterUser = userService.findByAccountId("ACCOUNTID_100").get();
		Spot afterSpot = afterUser.getRegisteredSpots().get(0);
		
		
		//The field of the changed Spot must be the same as the field of the Modification Form.
		assertEquals(spotModificationForm.getSpotName(), afterSpot.getSpotName(), 
				"변경된 장소의 이름은 폼의 장소 이름과 같아야 합니다.");
		assertEquals(spotModificationForm.getSpotIntroduction(), afterSpot.getSpotIntroduction(), 
				"변경된 장소의 소개는 폼의 장소 소개와 같아야 합니다.");
		
		
		//All Img entities of the Spot before the modification must be deleted.
		assertNull(uploadFileInfoService.findById(beforeMainSpotImgInfo.getId()), "이전의 장소 메인 사진은 삭제 되어야합니다.");
		for(UploadFileInfo beforeAddedSpotImgInfo : beforeAddedSpotImgInfos)
		{
			assertNull(uploadFileInfoService.findById(beforeAddedSpotImgInfo.getId()), "이전의 장소의 추가 사진은 삭제 되어야합니다.");
		}
		
		//The number of AddedSpotImgs must be 6 or less.
		List<AddedSpotImg> beforeAddedSpotImgList = 
				em.createQuery("select asi from AddedSpotImg asi where asi.spot =:spot", AddedSpotImg.class)
				.setParameter("spot", afterSpot).getResultList();
		assertTrue((beforeAddedSpotImgList.size()<=6), "AddedSpotImgs의 수는 6이하가 되어야 합니다.");
		
		
	}
	
	@Order(2)
	@Test
	@Transactional
	@DisplayName("장소 삭제 테스트")
	void testDeleteSpot() {
		
		//Initialize the list of entities related to the Spot before the Spot is deleted.
		User beforeUser = userService.findByAccountId("ACCOUNTID_9").get();
		Spot beforeSpot = beforeUser.getRegisteredSpots().get(0);
		List<AddedSpotImg> beforeAddedSpotImgs = beforeSpot.getAddedSpotImgs();
		ArrayList<UploadFileInfo> beforeAddedSpotImgInfos = new ArrayList<UploadFileInfo>();
		for(AddedSpotImg beforeAddedSpotImg : beforeAddedSpotImgs)
		{
			beforeAddedSpotImgInfos.add(beforeAddedSpotImg.getUploadFileInfo());
		}
		List<Comment> beforeComments = beforeSpot.getComments();
		
		HashSet<User> beforeVisitorSet = new HashSet<User>();
		for(Comment beforeComment : beforeComments)
		{
			beforeVisitorSet.add(beforeComment.getCommentUser());
		}
												
		//deleteSpot
		spotController.deleteSpot(beforeUser.getAccountId(), beforeSpot.getId());
		
		//before Entity Detached
		em.flush();
		em.clear();
		
		
		//Is the main image entity deleted?
		String beforeMainImgInfoId = beforeSpot.getMainSpotImgInfo().getId();
		UploadFileInfo MainImgInfo = uploadFileInfoService.findById(beforeMainImgInfoId);
		assertNull(MainImgInfo, "MainImgInfo 엔터티가 삭제되어 있어야 합니다.");
		
		//Are the AddedSpotImg entities deleted?
		for(AddedSpotImg beforeAddedSpotImg : beforeAddedSpotImgs) {
			Optional<AddedSpotImg> beforeAddedSpotImgOptioanl = addedSpotImgService.findById(beforeAddedSpotImg.getId());
			assertTrue(beforeAddedSpotImgOptioanl.isEmpty(), "AddedSpotImg Entity가 삭제되어 있어야 합니다.");
		}

		//Are the addedSpotImgInfo entities deleted?
		for(UploadFileInfo beforeAddedSpotImgInfo : beforeAddedSpotImgInfos)
		{
			assertNull(uploadFileInfoService.findById(beforeAddedSpotImgInfo.getId()),"AddedSpotImgInfo Entity가 삭제되어 있어야 합니다.");
		}
		
	
		//Has the comment entity been deleted?
		for(Comment beforeComment : beforeComments)
		{
			//refresh find
			Optional<Comment> afterCommentOptional = commentService.findById(beforeComment.getId());
			assertTrue(afterCommentOptional.isEmpty(), "Comment Entity는 삭제되어 있어야 합니다.");
			
			
		}
		
		//Have the visitorVisitedSpot entities been deleted?
		for(User beforeVisitor : beforeVisitorSet)
		{
			Optional<VisitorVisitedSpot> vvsOptioanl = visitorVisitedSpotService.findOneByUserAndSpot(beforeVisitor, beforeSpot);
			assertTrue(vvsOptioanl.isEmpty(), "VisitorVisitedSpot Entity는 삭제되어 있어야 합니다.");
		}
		
		
		//Is the Spot Entity deleted?
		Optional<Spot> beforeSpotOptioanl = spotService.findById(beforeSpot.getId());
		assertTrue(beforeSpotOptioanl.isEmpty(), "Spot Entity는 삭제되어 있어야 합니다.");
	
		
	}
	
	@Order(3)
	@Test
	@Transactional
	@DisplayName("유저 삭제 테스트")
	void testDeleteUser(@Mock BindingResult bindingResult, @Mock Model model, @Mock HttpServletRequest request,@Mock HttpSession httpSession)
	{
		/*
		 * check list
		 * 1. loginUser's Comments
		 * 2. loginUser's VitorVisitedSpot
		 * 3. loginUser's UserAcquaintance
		 * 4. loginUser's Spot
		 * 5. loginUser's UploadFileInfo
		 * 6. loginUser's AddedSpotImg
		 * 7. loginUser's RelationshipRequestService
		 * 8. loginUser's User
		 */
		//Init Before Datas
		
		//8
		User beforeUser = userService.findByAccountId("ACCOUNTID_8").get();
		//1
		List<Comment> beforeCommentList= commentService.getAllCommentListByUser(beforeUser);
		//2
		List<VisitorVisitedSpot> beforeVisitorVisitedSpotList = visitorVisitedSpotService.findByUser(beforeUser);
		//3
		List<UserAcquaintance> beforeUserAcquaintanceList = userAcquaintanceService.findByUser(beforeUser);
		//4
		List<Spot> beforeSpotList = spotService.getAllRegisteredSpotsByUser(beforeUser);
		//5
		ArrayList<UploadFileInfo> beforeUploadFileInfoList = new ArrayList<UploadFileInfo>();
		//6
		ArrayList<AddedSpotImg> beforeAddedSpotImgList = new ArrayList<AddedSpotImg>();
		//5-6
		for(Spot beforeSpot : beforeSpotList)
		{
			UploadFileInfo mainSpotImgInfo = beforeSpot.getMainSpotImgInfo();
			beforeUploadFileInfoList.add(mainSpotImgInfo);
			
			List<AddedSpotImg> addedSpotImgList = beforeSpot.getAddedSpotImgs();
			for(AddedSpotImg addedSpotImg : addedSpotImgList)
			{
				beforeAddedSpotImgList.add(addedSpotImg);
				beforeUploadFileInfoList.add(addedSpotImg.getUploadFileInfo());
			}
		}
		//add ProfileImg UploadFileInfo Entity
		beforeUploadFileInfoList.add(beforeUser.getProfileImgInfo());
		//7
		ArrayList<RelationshipRequest> beforeRelationshipRequestList = new ArrayList<RelationshipRequest>();
		
		relationshipRequestService.getReceivedRelationshipRequest(beforeUser, beforeUser).stream().forEach(beforeRelationshipRequestList::add);
		relationshipRequestService.getSendedRelationshipRequest(beforeUser, beforeUser).stream().forEach(beforeRelationshipRequestList::add);
		
		WithdrawForm withdrawForm = new WithdrawForm();
		withdrawForm.setPassword(beforeUser.getPassword());
		
		when(request.getSession(false)).thenReturn(httpSession);
		
		//delete User
		loginController.withdraw(beforeUser.getAccountId(),withdrawForm,bindingResult, model,request);
		
		
		
		//validate
		
		//1
		for(Comment  beforeComment : beforeCommentList)
		{
			assertNull(em.find(Comment.class, beforeComment.getId()), "Comment는 DB에서 삭제되어 있어야 합니다.");
		}
		//2
		for(VisitorVisitedSpot beforeVisitorVisitedSpot : beforeVisitorVisitedSpotList)
		{
			assertNull(em.find(VisitorVisitedSpot.class, beforeVisitorVisitedSpot.getId()), "VisitorVisitedSpot는 DB에서 삭제되어 있어야 합니다.");
		}
		//3
		for(UserAcquaintance beforeUserAcquaintance : beforeUserAcquaintanceList)
		{
			assertNull(em.find(UserAcquaintance.class, beforeUserAcquaintance.getId()), "UserAcquaintance는 DB에서 삭제되어 있어야 합니다.");

		}
		//4
		for(Spot beforeSpot : beforeSpotList)
		{
			assertNull(em.find(Spot.class, beforeSpot.getId()),"Spot는 DB에서 삭제되어 있어야 합니다.");
		}
		//5
		for(UploadFileInfo beforeUploadFileInfo : beforeUploadFileInfoList)
		{
			assertNull(em.find(UploadFileInfo.class, beforeUploadFileInfo.getId()),"UploadFileInfo는 DB에서 삭제되어 있어야 합니다.");
		}
		//6
		for(AddedSpotImg beforeAddedSpotImg : beforeAddedSpotImgList)
		{
			assertNull(em.find(AddedSpotImg.class, beforeAddedSpotImg.getId()),"AddedSpotImg는 DB에서 삭제되어 있어야 합니다.");

		}
		//7
		for(RelationshipRequest beforeRelationshipRequest : beforeRelationshipRequestList)
		{
			assertNull(em.find(RelationshipRequest.class, beforeRelationshipRequest.getId()),"RelationshipRequest는 DB에서 삭제되어 있어야 합니다.");

		}
		//8
		assertNull(em.find(User.class, beforeUser.getId()),"User는 DB에서 삭제되어 있어야 합니다.");
				
	}
	

	

}
