package my.ourShef.controller.validator;

import org.springframework.stereotype.Component;

@Component
public class FileExtValidator {
	
	public boolean isNotImgFile(String lowerCaseExt) {
		return !lowerCaseExt.equals("jpg") && !lowerCaseExt.equals("jpeg") && !lowerCaseExt.equals("png");
	}

}
