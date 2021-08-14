package my.ourShef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;


@Controller
@RequestMapping("/aquaintance")
@Slf4j
@RequiredArgsConstructor
public class AcquaintanceContoller {

	@GetMapping("/myAcquaintanceList")
	public String myAcquaintanceList(
			@SessionAttribute(name=SessionConst.LOGIN_USER_ACCOUNT_ID, required = true) String LoginUserAccountId)
	{
		return "/acquaintance/acquaintanceList";
	}
}
