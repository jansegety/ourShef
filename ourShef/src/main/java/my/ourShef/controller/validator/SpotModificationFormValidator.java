package my.ourShef.controller.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import my.ourShef.controller.form.SpotModificationForm;

@Component
public class SpotModificationFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {

		return SpotModificationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		SpotModificationForm spotModificationForm = (SpotModificationForm) target;

		// Img validation
		if (!spotModificationForm.getSpotMainImg().isEmpty()) {
			String temp = spotModificationForm.getSpotMainImg().getOriginalFilename(); // ex 7.jpg
			String ext = temp.substring(temp.lastIndexOf(".") + 1); // 확장자 얻기
			String lowerCaseExt = ext.toLowerCase();
			if (!lowerCaseExt.equals("jpg") && !lowerCaseExt.equals("png")) {
				errors.rejectValue("spotMainImg", "only.img.org.springframework.web.multipart.MultipartFile");
			}

		}

		// If additional images exist
		if (!spotModificationForm.getSpotAddedImgs().get(0).isEmpty()) {

			boolean isImg = true;

			List<MultipartFile> spotAddedImgs = spotModificationForm.getSpotAddedImgs();
			for (MultipartFile spotAddedImg : spotAddedImgs) {
				String temp = spotAddedImg.getOriginalFilename(); // ex 7.jpg
				String ext = temp.substring(temp.lastIndexOf(".") + 1); // 확장자 얻기
				String lowerCaseExt = ext.toLowerCase();
				if (!lowerCaseExt.equals("jpg") && !lowerCaseExt.equals("png")) {
					isImg = false;
				}
			}

			if (isImg == false) {
				errors.rejectValue("spotAddedImgs", "only.img.org.springframework.web.multipart.MultipartFile");
			}

		}

	}
}
