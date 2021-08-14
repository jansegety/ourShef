package my.ourShef.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import my.ourShef.controller.form.SpotRegisterationForm;


@Component
public class SpotRegisterationFormValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return SpotRegisterationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		
		SpotRegisterationForm spotRegisterationForm= (SpotRegisterationForm)target;
		
	
		
		//만약 mainSpotImg가 없다면
		if(spotRegisterationForm.getSpotMainImg().isEmpty()) {
			errors.rejectValue("spotMainImg", "NotEmpty");
		}
		
	}

	
}
