package my.ourShef.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.controller.form.JoinForm;
import my.ourShef.controller.validator.JoinFormValidator;

@Controller
@RequestMapping("/login")
@Slf4j
@RequiredArgsConstructor
public class LoginContoller {
	
	private final JoinFormValidator joinFormValidator;
	
	//@IntiBinder->해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.
	@InitBinder //요청이 올 때마다 dataBinder는 새로 만들어진다. //이 컨트롤러에서만 적용된다.
	public void init(WebDataBinder dataBinder) {
		dataBinder.addValidators(joinFormValidator);
	}
	

	@GetMapping("/join")
	public String createForm(Model model) {
		model.addAttribute("joinForm", new JoinForm());
		return "login/joinForm";
	}
	
	
	@PostMapping("/join")
	public String create(@Validated @ModelAttribute JoinForm joinForm, BindingResult bindingResult, Model model){
		System.out.println("들어온 데이터 = " + joinForm);
		

		
		//검증에 실패하면 다시 입력 폼으로
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "login/joinForm";
		}
		
		
		//성공 로직
		
		
		return "redirect:/";
	}
	
	
	
}
