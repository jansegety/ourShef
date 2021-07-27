package my.ourShef.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.domain.User;
import my.ourShef.service.UserService;
import my.ourShef.session.SessionManager;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

	private final UserService userService;
	private final SessionManager sessionManager;
	
	
	//@SessionAttribute 스프링이 제공하는 이 기능은 세션을 생성하지 않기 때문에, 세션을 찾아올 때 사용하면 된다.
	@RequestMapping("/")
	public String home(@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = false) String LoginUserAccountId, Model model) {
		
		/*
		 * 세션 관리자에 저장된 회원 정보 조회
		 */
//		String userAccountId = (String)sessionManager.getSession(request);
//		
//		if(userAccountId == null) {
//			return "home";	
//		}
		
			
		
		//회원이 정보 DB에서 가져오기
		Optional<User> findByAccountId = userService.findByAccountId(LoginUserAccountId);
		
		//세션은 존재하지만 회원정보가 삭제되었을 경우
		if(findByAccountId.isEmpty()) {
			return "home";
		}
			
		model.addAttribute("user", findByAccountId.get());
	
		//login
		return "login/loginHome";
	}
	
	
	
}