package my.ourShef.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;
import my.ourShef.service.UserService;

@Slf4j
public class UserExistCheckInterceptor implements HandlerInterceptor{
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID) != null)
		{
			String LoginUserAccountId = (String)session.getAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID);
			if(userService.findByAccountId(LoginUserAccountId).isPresent())
			{
				return true;
			}
			else
			{
				//Session exists but user does not exist	
				response.sendRedirect("/login/login?redirectURL=" + requestURI);
				return false;
			}
				
		}
		
		response.sendRedirect("/login/login?redirectURL=" + requestURI);
		return false;
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if(ex != null) {
			log.error("error info : ", ex); //no '{}'needed for error
		}
	}

	
	
}
