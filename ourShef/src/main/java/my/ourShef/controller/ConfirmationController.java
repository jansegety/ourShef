package my.ourShef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/confirmation")
@RequiredArgsConstructor
public class ConfirmationController {
	
	@GetMapping("/deleteSpot")
	public String confirmationDeleteSpot() {
		return "/confirm/spotDeleteConfirm";
	}

}
