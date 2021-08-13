package my.ourShef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.controller.form.JoinForm;


@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	@GetMapping("/myInfo")
	public String myInfo(Model model) {
		
		model.addAttribute("joinForm", new JoinForm());
		
		return "user/loginUserInfo";
	}
	
}
