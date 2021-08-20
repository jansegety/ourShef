package my.ourShef.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
import my.ourShef.controller.validator.SpotModificationFormValidator;
import my.ourShef.controller.validator.SpotRegisterationFormValidator;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.domain.bridge.AddedSpotImg;
import my.ourShef.file.FileStore;
import my.ourShef.service.CommentService;
import my.ourShef.service.SpotService;
import my.ourShef.service.UploadFileInfoService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.AddedSpotImgService;

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
	private InitDb.InitService initService;
	@Autowired
	private UserService userService;
	@Autowired
	private UploadFileInfoService uploadFileInfoService;
	@Autowired
	private EntityManager em;


	@Rollback(false)
	@Order(1)
	@Test
	@Transactional
	@DisplayName("장소 등록 테스트")
	void spotRegisterationTest(@Mock BindingResult bindingResult, @Mock Model model) throws IOException {
		
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
	void testModifySpot(@Mock BindingResult bindingResult, @Mock Model model, @Mock RedirectAttributes redirectAttributes) throws IOException {
		
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
		spotController.modifySpot(model, spotModificationForm, bindingResult, beforeSpot.getId(), redirectAttributes);
		
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
	
	
	
	
	

}
