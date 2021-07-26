package my.ourShef.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import my.ourShef.domain.User;

class SessionManagerTest {
	
	SessionManager sessionManager = new SessionManager(); 

	@Test
	void sessionTest() {
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		//세션 생성
		User user = new User("testUser");
		sessionManager.createSession(user, response);
		
		//요청에 응답 쿠키 저장
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(response.getCookies());
		
		//세션 조회
		Object result = sessionManager.getSession(request);
		assertThat(result).isEqualTo(user);
		
		//세션 만료
		sessionManager.expire(request);
		Object expired = sessionManager.getSession(request);
		
		assertThat(expired).isNull();
		
	}

}
