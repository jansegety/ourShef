package my.ourShef.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.User;
import my.ourShef.service.UserService;
import my.ourShef.session.SessionManager;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

	private final UserService userService;
	private final SessionManager sessionManager;
	
	@RequestMapping("/")
	public String home(HttpServletRequest request, Model model) {
		
		//세션 관리자에 저장된 회원 정보 조회
		String userAccountId = (String)sessionManager.getSession(request);
		
		if(userAccountId == null) {
			return "home";	
		}
		
		//회원이 정보 DB에서 가져오기
		Optional<User> findByAccountId = userService.findByAccountId(userAccountId);
		
		//세션은 존재하지만 회원정보가 삭제되었을 경우
		if(findByAccountId.isEmpty()) {
			return "home";
		}
			
		model.addAttribute("user", findByAccountId.get());
	
		//login
		return "login/loginHome";
	}
	
	
	
}