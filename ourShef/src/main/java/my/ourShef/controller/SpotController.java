package my.ourShef.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.controller.form.SpotRegisterationForm;
import my.ourShef.controller.validator.SpotRegisterationFormValidator;

@Slf4j
@Controller
@RequestMapping("/spot")
@RequiredArgsConstructor
public class SpotController {

	private final SpotRegisterationFormValidator spotRegisterationFormValidator;
	
	@InitBinder //요청이 올 때마다 dataBinder는 새로 만들어진다. //이 컨트롤러에서만 적용된다.
	public void init(WebDataBinder dataBinder) {
		
		if(dataBinder.getTarget() == null) return;
		
		final List<Validator> validatorsList = new ArrayList<>();
		validatorsList.add(spotRegisterationFormValidator);
		
		for(Validator validator : validatorsList) {
			if(validator.supports(dataBinder.getTarget().getClass())) {
				dataBinder.addValidators(validator);
			}
		}

		
	}
	
	@GetMapping("/registeration")
	public String createForm(Model model) {
		model.addAttribute("spotRegisterationForm", new SpotRegisterationForm());
		return "spot/spotRegisteration";
	}
	
	@PostMapping("/registeration")
	public String registerSpot(@Validated @ModelAttribute SpotRegisterationForm spotRegisterationForm, BindingResult bindingResult) {
		
		log.info("spotInfo={} ", spotRegisterationForm);
		
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "spot/spotRegisteration";
		}
		
		
		return "redirect:/";
	}
	
	@GetMapping("/spot")
	public String spot() {
		
		return "spot/spot";
	}
	
	
	
}
