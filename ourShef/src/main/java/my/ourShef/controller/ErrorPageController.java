package my.ourShef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/error-page")
@Controller
public class ErrorPageController {

	
	@RequestMapping("/fileSizeLimitExceeded")
	public String errorPageFileSizeLimitExceeded() {
		
		return "error/fileSizeLimitExceeded";
	}
	
}
