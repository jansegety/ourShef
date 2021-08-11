package my.ourShef.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.PatternMatchUtils;

import lombok.extern.slf4j.Slf4j;
import my.ourShef.SessionConst;

@Slf4j
public class LoginCheckFilter implements Filter{
	
	private static final String[] whitelist = {"/","/login/join","/login/login","/login/logout","/img/*","/css/*","/js/*","/bootStrap/*","/library/*","/library/fontawesome/*","/common.js","/style.css"};
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String requestURI = httpRequest.getRequestURI();
		
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		try {
//			log.info("인증 체크 필터 시작{}", requestURI);
			if (isLoginCheckPath(requestURI)) {
//				log.info("인증 체크 로직 실행 {}", requestURI);
				HttpSession session = httpRequest.getSession(false);
				
				//세션이 없거나 세션에 사용자 계정 정보가 없으면
				if(session == null || session.getAttribute(SessionConst.LOGIN_USER_ACCOUNT_ID) == null) {
//					log.info("미인증 사용자 요청 {}", requestURI);
					//로그인으로 redirect
					httpResponse.sendRedirect("/login/login?redirectURL=" + requestURI);
					return;
				}
			}
			
			chain.doFilter(request, response);
			
		} catch(Exception e) {
			throw e; //예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야 함
		} finally {
			log.info("인증 체크 필터 종료 {}", requestURI);
		}
		
		
	}
	
	/*
	 * 화이트 리스트의 경우 인증 체크 X
	 */
	private boolean isLoginCheckPath(String requestURI) {
		return !PatternMatchUtils.simpleMatch(whitelist, requestURI); //화이트 리스트에 들어가면 false
	}


	
}
