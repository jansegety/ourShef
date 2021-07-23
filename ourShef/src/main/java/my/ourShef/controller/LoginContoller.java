package my.ourShef.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.controller.form.JoinForm;

@Controller
@RequestMapping("/login")
@Slf4j
//@RequiredArgsConstructor
public class LoginContoller {

	@GetMapping("/join")
	public String createForm(Model model) {
		model.addAttribute("joinForm", new JoinForm());
		return "login/joinForm";
	}
	
//	@PostMapping("/join")
//	public String create(@ModelAttribute JoinForm joinForm, BindingResult bindingResult, Model model){
//		System.out.println("들어온 데이터 = " + joinForm);
//		
//		
//		
//		//검증 오류 결과를 보관
//		Map<String, String> errors = new HashMap<>();
//		
//		//검증 로직
//		if(joinForm.getJoinProfileImg().isEmpty()) {
//			errors.put("joinProfileImgRequiredError", "프로필 이미지는 필수입니다.");
//			
//		}else {
//			String temp = joinForm.getJoinProfileImg().getOriginalFilename(); //ex 7.jpg
//			String ext = temp.substring(temp.lastIndexOf(".")+1); //확장자 얻기
//			String lowerCaseExt = ext.toLowerCase();
//			if(!lowerCaseExt.equals("jpg")&&!lowerCaseExt.equals("png"))
//			{
//				errors.put("joinProfileImgExtError", "이미지 파일만 가능합니다.");
//			}
//			
//		}
//		
//		if(!StringUtils.hasText(joinForm.getNickName())) {
//			errors.put("nickName", "닉네임은 필수입니다.");
//		}
//		if (joinForm.getPassword() == null || joinForm.getPassword().length()>16|| joinForm.getPassword().length()<8)
//		{
//			errors.put("password", "비밀번호 문자수는 8자리에서 16자리까지입니다.");
//		}
//		if (joinForm.getSelfIntroduction().length() >100)
//		{
//			errors.put("selfIntroduction", "100자 이하로 써주세요.");
//		}
//		
//		//특정 필드가 아닌 복합 룰 검증
//		//패스워드에 닉네임을 포함하고 있다면
//		if(joinForm.getPassword().length() != 0 && joinForm.getNickName().length() != 0)
//		{
//			if(joinForm.getPassword().contains(joinForm.getNickName())) {
//				errors.put("passwordContainsNickNameError", "패스워드는 닉네임을 포함할 수 없습니다.");
//			}
//		}
//		
//		//검증에 실패하면 다시 입력 폼으로
//		if(!errors.isEmpty()) {
//			model.addAttribute("errors", errors);
//			return "login/joinForm";
//		}
//		
//		
//		//성공 로직
//		
//		
//		return "redirect:/";
//	}
	
	@PostMapping("/join")
	public String create(@ModelAttribute JoinForm joinForm, BindingResult bindingResult, Model model){
		System.out.println("들어온 데이터 = " + joinForm);
		
		
		
		//검증 오류 결과를 보관
		Map<String, String> errors = new HashMap<>();
		
		//검증 로직
		if(joinForm.getJoinFormProfileImgFile().isEmpty()) {
			bindingResult.addError(new FieldError("joinForm","joinFormProfileImgFile", joinForm.getJoinFormProfileImgFile(), false, new String[]{"required.joinForm.profileImgFile"}, null, null));
			
		}else {
			String temp = joinForm.getJoinFormProfileImgFile().getOriginalFilename(); //ex 7.jpg
			String ext = temp.substring(temp.lastIndexOf(".")+1); //확장자 얻기
			String lowerCaseExt = ext.toLowerCase();
			if(!lowerCaseExt.equals("jpg")&&!lowerCaseExt.equals("png"))
			{
				bindingResult.addError(new FieldError("joinForm","joinFormProfileImgFile", joinForm.getJoinFormProfileImgFile(), false, new String[]{"only.img"}, null, null));
			}
			
		}
		
		if(!StringUtils.hasText(joinForm.getJoinFormNickName())) {
			bindingResult.addError(new FieldError("joinForm","joinFormNickName", joinForm.getJoinFormNickName(), false, new String[] {"required.joinForm.nickName"}, null, null));
		}
		if (joinForm.getJoinFormPassword() == null || joinForm.getJoinFormPassword().length()>16|| joinForm.getJoinFormPassword().length()<8)
		{
			bindingResult.addError(new FieldError("joinForm","joinFormPassword", joinForm.getJoinFormPassword(), false, new String[] {"range.password"}, new Object[] {8, 16}, null));
		}
		if (joinForm.getJoinFormSelfIntroduction().length() >100)
		{
			bindingResult.addError(new FieldError("joinForm","joinFormSelfIntroduction", joinForm.getJoinFormSelfIntroduction(), false, new String[] {"min.text"}, new Object[] {100}, null));
		}
		
		//특정 필드가 아닌 복합 룰 검증
		//패스워드에 닉네임을 포함하고 있다면
		if(joinForm.getJoinFormPassword().length() != 0 && joinForm.getJoinFormNickName().length() != 0)
		{
			if(joinForm.getJoinFormPassword().contains(joinForm.getJoinFormNickName())) {
				bindingResult.addError(new ObjectError("joinForm", new String[] {"dont.passwordContainsNickName"}, null, null));
			}
		}
		
		//검증에 실패하면 다시 입력 폼으로
		if(bindingResult.hasErrors()) {
			log.info("errors={} ", bindingResult);
			return "login/joinForm";
		}
		
		
		//성공 로직
		
		
		return "redirect:/";
	}
	
	
	
}
