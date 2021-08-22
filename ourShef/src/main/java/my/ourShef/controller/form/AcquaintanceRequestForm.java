package my.ourShef.controller.form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AcquaintanceRequestForm {

	@Size(max=20)
	String acquaintanceAccountId;
	
}
