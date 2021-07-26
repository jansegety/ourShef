package my.ourShef.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/*
 * 세션관리
 */
@Component
public class SessionManager {

	//ConcurrentHashMap은 동시성을 보장한다.
	public static final String SESSION_COOKIE_NAME = "OurShef_Session";
	private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
	
	/*
	 * 세션 생성
	 * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
	 * 세션 저장소에 sessionId와 보관할 값 저장
	 * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
	 * 
	 */
	public void createSession(Object value, HttpServletResponse response) {
		//세션 id를 생성하고, 값을 세션에 저장
		String sessionId = UUID.randomUUID().toString();
		sessionStore.put(sessionId, value);
		
		//쿠키 생성
		Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		sessionCookie.setPath("/"); //path를 설정해주지 않으면 쿠키가 등록되는 부모주소에서만 쿠기가 재전송 된다.
		response.addCookie(sessionCookie);
		
	}
	/*
	 * 세션 조희
	 */
	public Object getSession(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie == null) {
			return null;
		}
		return sessionStore.get(sessionCookie.getValue());
		
	}
	
	/*
	 * 세션 만료
	 */
	public void expire(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie != null) {
			sessionStore.remove(sessionCookie.getValue());
		}
	}
	
	
	public Cookie findCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			return null;
		}
		return Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findAny().orElse(null);
	}
	
}
