package my.ourShef.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.controller.dto.Acquaintance;
import my.ourShef.controller.dto.LoginUserDto;
import my.ourShef.controller.dto.LoginUserRecentSpotDto;
import my.ourShef.controller.dto.RecentAcquaintanceSpotDto;
import my.ourShef.controller.dto.RecentSpot;
import my.ourShef.controller.pager.SpotPager;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
import my.ourShef.service.SpotService;
import my.ourShef.service.UserService;
import my.ourShef.service.bridge.VisitorVisitedSpotService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

	private final UserService userService;
	private final SpotService spotService;
	private final VisitorVisitedSpotService visitorVisitedSpotService;

	// @SessionAttribute 스프링이 제공하는 이 기능은 세션을 생성하지 않기 때문에, 세션을 찾아올 때 사용하면 된다.
	@RequestMapping("/")
	public String home(
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = false) String LoginUserAccountId,
			Model model) {

		// Get user entity from DB
		Optional<User> findUserOptional = userService.findByAccountId(LoginUserAccountId);

		// If a session exists but user information is deleted
		if (findUserOptional.isEmpty()) {

			return "home";
		}
		
		User findUser = findUserOptional.get();
		
		// setting LoginUserDto
		setLoginUserDto(model, findUser);

		// setting LoginUserRecentSpotDto
		// Get user's most recent registered spot information
		setLoginUserRecentSpotDto(LoginUserAccountId, model);

		// setting RecentAquaintanceSpotsList
		// default tupleNumByPage=5 pageNumByGroup=5 currentPage= 1
		setRecentAquaintanceSpotPagerAndDtoList(model, findUser, 5L, 5L, 1L);
		
		// login
		return "login/loginHome";
	}
	
	@GetMapping("/loginHome")
	public String loginHomeByPage(@RequestParam(value="page", required=false) Long page,
			@SessionAttribute(name = SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId,
			Model model) {
		
		//If connect without parameter page
		if(page==null)
		{
			return "redirect:/"; 
		}
		
		// Get user entity from DB
		Optional<User> findUserOptional = userService.findByAccountId(LoginUserAccountId);
		User findUser = findUserOptional.get();
		
		// setting LoginUserDto
		setLoginUserDto(model, findUser);

		// setting LoginUserRecentSpotDto
		// Get user's most recent registered spot information
		setLoginUserRecentSpotDto(LoginUserAccountId, model);

		// setting RecentAquaintanceSpotsList
		// default tupleNumByPage=5 pageNumByGroup=5 currentPage= page
		setRecentAquaintanceSpotPagerAndDtoList(model, findUser, 5L, 5L, page);
		
		
		
		return "login/loginHome";
	}
	

	private void setRecentAquaintanceSpotPagerAndDtoList(Model model, User findUser, Long tupleNumByPage, Long pageNumByGroup, Long currentPage) {
		Long totalTupleNum = userService.getAcquaintanceSpotTotalNum(findUser);
		SpotPager spotPager = new SpotPager(tupleNumByPage, pageNumByGroup, currentPage, totalTupleNum);
		model.addAttribute("spotPager", spotPager);
		Long offset = (currentPage-1)*tupleNumByPage; //because index is num-1
		
		setRecentAquaintanceSpotDtoList(model, findUser, tupleNumByPage, offset);
	}

	private void setRecentAquaintanceSpotDtoList(Model model, User findUser, Long limit, Long offset) {
		
		
		List<Object[]> recentAcquaintanceSpotList = userService.getRecentAcquaintanceSpotList(findUser, limit, offset);
		List<RecentAcquaintanceSpotDto> recentAcquaintanceSpotDtoList = new ArrayList<RecentAcquaintanceSpotDto>();

		for (Object[] recentAcquaintanceSpot : recentAcquaintanceSpotList) {
			
			//set Acquaintance
			User user = (User)recentAcquaintanceSpot[0];
			Acquaintance acquaintance = new Acquaintance();
			acquaintance.setId(user.getId());
			acquaintance.setNickName(user.getNickName());
			acquaintance.setIntroduction(user.getIntroduction());
			acquaintance.setProfileImgStoreName(user.getProfileImgInfo().getStoreFileName());
			acquaintance.setReliability(user.getReliability());
			
			//set RecentSpot
			Spot spot = (Spot)recentAcquaintanceSpot[1];
			RecentSpot recentSpot = new RecentSpot();
			recentSpot.setId(spot.getId());
			recentSpot.setSpotName(spot.getSpotName());
			recentSpot.setSpotIntroduction(spot.getSpotIntroduction());
			recentSpot.setMainImgStoreName(spot.getMainSpotImgInfo().getStoreFileName());
			recentSpot.setRegisteredTime(spot.getRegisteredTime());
			recentSpot.setRegistrantStarPoint(spot.getRegistrantStarPoint());
			recentSpot.setUsersStarPoint(spot.getUsersStarPoint());
			recentSpot.setVisits(spot.getVisits());
			recentSpot.setIsVisited((Boolean)visitorVisitedSpotService.isVisitedSpotByTheUser(spot, findUser));
			
			//set RecentAcquaintanceSpotDto
			RecentAcquaintanceSpotDto recentAcquaintanceSpotDto = new RecentAcquaintanceSpotDto();
			recentAcquaintanceSpotDto.setAcquaintance(acquaintance);
			recentAcquaintanceSpotDto.setRecentSpot(recentSpot);
			
			recentAcquaintanceSpotDtoList.add(recentAcquaintanceSpotDto);
		}
		model.addAttribute("recentAcquaintanceSpotDtoList", recentAcquaintanceSpotDtoList);
	}

	private void setLoginUserRecentSpotDto(String LoginUserAccountId, Model model) {
		Optional<Spot> RecentRegisterationSpotOtioanl = spotService
				.findRecentRegisterationSpotByUserAccountId(LoginUserAccountId);
		if (RecentRegisterationSpotOtioanl.isPresent()) {
			Spot recentRegisterationSpot = RecentRegisterationSpotOtioanl.get();
			LoginUserRecentSpotDto loginUserRecentSpotDto = new LoginUserRecentSpotDto();
			loginUserRecentSpotDto.setSpotId(recentRegisterationSpot.getId());
			loginUserRecentSpotDto.setRegisteredTime(recentRegisterationSpot.getRegisteredTime());
			loginUserRecentSpotDto.setSpotName(recentRegisterationSpot.getSpotName());
			loginUserRecentSpotDto.setSpotIntroduction(recentRegisterationSpot.getSpotIntroduction());
			loginUserRecentSpotDto.setRegistrantStarPoint(recentRegisterationSpot.getRegistrantStarPoint());
			loginUserRecentSpotDto.setUsersStarPoint(recentRegisterationSpot.getUsersStarPoint());
			loginUserRecentSpotDto.setVisits(recentRegisterationSpot.getVisits());

			model.addAttribute("loginUserRecentSpotDto", loginUserRecentSpotDto);
		}
		
	}

	private void setLoginUserDto(Model model, User findUser) {
		LoginUserDto loginUserDto = new LoginUserDto();

		loginUserDto.setId(findUser.getId());
		loginUserDto.setNickName(findUser.getNickName());
		loginUserDto.setIntroduction(findUser.getIntroduction());
		loginUserDto.setReliability(findUser.getReliability());

		model.addAttribute("loginUserDto", loginUserDto);
	}

}