package my.ourShef.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;



import my.ourShef.controller.form.JoinForm;

@SpringBootTest
public class BeanValidationTest {

	@Test
	void beanValidation() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		JoinForm joinForm = new JoinForm();
		
		MultipartFile multi;
		try {
			File file = new File("./test.txt");
			multi = new MockMultipartFile("joinFormProfileImgFile", "test.txt", null, new FileInputStream(file));
			joinForm.setJoinFormProfileImgFile(multi); //텍스트 파일 삽입
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		joinForm.setJoinFormPassword("123123"); //정해진 길이보다 적게 입력
		
		
		Set<ConstraintViolation<JoinForm>> violations = validator.validate(joinForm);
		
		System.out.println("파일 이름은 = "+joinForm.getJoinFormProfileImgFile().getOriginalFilename());
		
		for(ConstraintViolation<JoinForm> violation : violations) {
			System.out.println("violation = " + violation);
			System.out.println("violation = " + violation.getMessage());

		}
		
	
	}
	
}
