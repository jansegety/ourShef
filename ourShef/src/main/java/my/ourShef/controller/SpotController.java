package my.ourShef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import my.ourShef.controller.form.JoinForm;

@Controller
@RequestMapping("/spot")
public class SpotController {

	@GetMapping("/registeration")
	public String createForm(Model model) {
		model.addAttribute("joinForm", new JoinForm());
		return "spot/spotRegisteration";
	}
	
	@PostMapping("/registeration")
	public String registerSpot() {
		
		
		return "redirect:/";
	}
	
}
