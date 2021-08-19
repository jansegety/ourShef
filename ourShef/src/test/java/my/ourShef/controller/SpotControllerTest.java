package my.ourShef.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import my.ourShef.InitDb;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.form.SpotRegisterationForm;
import my.ourShef.controller.validator.SpotModificationFormValidator;
import my.ourShef.controller.validator.SpotRegisterationFormValidator;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
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
	private EntityManager em;

	@Rollback(false)
	@Test
	@Transactional
	@DisplayName("장소 수정")
	void spotModificationTest(@Mock BindingResult bindingResult) throws IOException {
		
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
			
			//validation registeration spot
			Optional<User> findUserOptioanl = userService.findById(user.getId());
			User findUser = findUserOptioanl.get();
			List<Spot> registeredSpots = findUser.getRegisteredSpots();
			
			Assert.notEmpty(registeredSpots, "registeredSpots는 원소를 가져야 합니다.");
			for(int i=0; i < spotRegisterationFormList.size(); i++) {
				assertEquals(registeredSpots.get(i).getSpotName(),spotRegisterationFormList.get(i).getSpotName());
			}
		}
		
		
		
		
		
		
		

	}
	
	
	@Test
	void testModifySpot() {
		
	}
	
	
	

}
