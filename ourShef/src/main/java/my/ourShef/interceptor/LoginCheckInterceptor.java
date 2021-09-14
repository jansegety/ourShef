package my.ourShef.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.service.UserService;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
		
		try {

				HttpSession session = request.getSession(false);
				
				//If there is no session or the session does not contain user account information
				if(session == null || session.getAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID) == null) {

					//로그인으로 redirect
					response.sendRedirect("/login/login?redirectURL=" + requestURI);
					return false;
				}
									
			return true;
			
		} catch(Exception e) {
			throw e; //예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야 함
		} finally {
			log.info("인증 체크 인터셉터 종료 {}", requestURI);
		}
		
		
	}
	


	
	
}
