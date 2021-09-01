package my.ourShef;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import my.ourShef.filter.LogFilter;
import my.ourShef.filter.LoginCheckFilter;
import my.ourShef.interceptor.UserExistCheckInterceptor;

/*
 * 필터를 스프링부트를 통해 내장 WAS에 등록합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(userExistCheckInterceptor())
		.order(1)
		.addPathPatterns("/**")
		.excludePathPatterns("/","/login/join","/login/login","/login/logout","/img/*","/css/*",
				"/js/*","/bootStrap/*","/library/*","/library/fontawesome/*","/common.js","/common.css",
				"/confirmation/createAccount","/confirmation/deleteAccount","/error");
	}
	
	@Bean
	public UserExistCheckInterceptor userExistCheckInterceptor() {
		return new UserExistCheckInterceptor();
	}

	@Bean
	public FilterRegistrationBean logFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new LogFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*");
		
		return filterRegistrationBean;	
	}
	
	@Bean
	public FilterRegistrationBean loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new LoginCheckFilter());
		filterRegistrationBean.setOrder(2);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
		
		return filterRegistrationBean;	
	}
	
	
}
