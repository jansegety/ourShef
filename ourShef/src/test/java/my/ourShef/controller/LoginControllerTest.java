package my.ourShef.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import my.ourShef.InitDb;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.domain.User;
import my.ourShef.service.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

	@Autowired
	private LoginController loginController;
	@Autowired
	private InitDb.InitService initService;
	@Autowired
	private UserService userService;
	
	
	@Mock
	private BindingResult bindingResult;
	
	@Test
	@DisplayName("유저 가입 테스트")
	void testCreate() throws Exception {
		
		//ACCOUNTID_100~ACCOUNTID_109
		ArrayList<JoinForm> joinFormList = initService.makeJoinFormByRange(100, 110);
		
		
		
		for(JoinForm joinForm : joinFormList)
		{
			loginController.create(joinForm, bindingResult);
			
			Optional<User> userOptioanl = userService.findByAccountId(joinForm.getJoinFormAccountId());
			assertNotNull(userOptioanl.get());
			
		}
		
	}

}
