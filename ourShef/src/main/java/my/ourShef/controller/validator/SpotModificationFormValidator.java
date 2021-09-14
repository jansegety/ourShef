package my.ourShef.controller.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import my.ourShef.controller.form.SpotModificationForm;

@Component
@RequiredArgsConstructor
public class SpotModificationFormValidator implements Validator {
	
	private final FileExtValidator fileExtValidator;

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
			if (fileExtValidator.isNotImgFile(lowerCaseExt)) {
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
				if (fileExtValidator.isNotImgFile(lowerCaseExt)) {
					isImg = false;
				}
			}

			if (isImg == false) {
				errors.rejectValue("spotAddedImgs", "only.img.org.springframework.web.multipart.MultipartFile");
			}

		}

	}
}
