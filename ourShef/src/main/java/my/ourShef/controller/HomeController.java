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
import my.ourShef.controller.dto.LoginUserDto;
import my.ourShef.controller.dto.LoginUserRecentSpotDto;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.service.SpotService;
import my.ourShef.service.UserService;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

	private final UserService userService;
	private final SpotService spotService;
	
	
	
	//@SessionAttribute 스프링이 제공하는 이 기능은 세션을 생성하지 않기 때문에, 세션을 찾아올 때 사용하면 된다.
	@RequestMapping("/")
	public String home(
			@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = false) String LoginUserAccountId
			, Model model) {
		
		
		
		//Get user entity from DB
		Optional<User> findByAccountId = userService.findByAccountId(LoginUserAccountId);
		
		//If a session exists but user information is deleted
		if(findByAccountId.isEmpty()) {
		
			return "home";
		}
		
		//setting LoginUserDto
		User findUser = findByAccountId.get();
		LoginUserDto loginUserDto = new LoginUserDto();
		
		loginUserDto.setNickName(findUser.getNickName());
		loginUserDto.setIntroduction(findUser.getIntroduction());
		loginUserDto.setReliability(findUser.getReliability());
		
		model.addAttribute("loginUserDto", loginUserDto);
		
		//setting LoginUserRecentSpotDto
		//Get user's most recent registered spot information
			Optional<Spot> RecentRegisterationSpotOtioanl = spotService.findRecentRegisterationSpotByUserAccountId(LoginUserAccountId);
			if(RecentRegisterationSpotOtioanl.isPresent())
			{
				Spot recentRegisterationSpot = RecentRegisterationSpotOtioanl.get();
				LoginUserRecentSpotDto loginUserRecentSpotDto = new LoginUserRecentSpotDto();
				loginUserRecentSpotDto.setRegisteredTime(recentRegisterationSpot.getRegisteredTime());
				loginUserRecentSpotDto.setSpotName(recentRegisterationSpot.getSpotName());
				loginUserRecentSpotDto.setSpotIntroduction(recentRegisterationSpot.getSpotIntroduction());
				loginUserRecentSpotDto.setRegistrantStarPoint(recentRegisterationSpot.getRegistrantStarPoint());
				loginUserRecentSpotDto.setUsersStarPoint(recentRegisterationSpot.getUsersStarPoint());
				
				model.addAttribute("loginUserRecentSpotDto", loginUserRecentSpotDto);
			}
		
		//	
		
		//login
		return "login/loginHome";
	}
	
	
	
}