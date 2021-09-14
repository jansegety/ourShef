package my.ourShef;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import my.ourShef.filter.LogFilter;
import my.ourShef.interceptor.LoginCheckInterceptor;
import my.ourShef.interceptor.UserExistCheckInterceptor;

/*
 * 필터를 스프링부트를 통해 내장 WAS에 등록합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Value("${loginCheck.whiteList}")
	private String[] whiteList;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(loginCheckInterceptor())
		.order(1)
		.addPathPatterns("/**")
		.excludePathPatterns(whiteList);
		
		registry.addInterceptor(userExistCheckInterceptor())
		.order(2)
		.addPathPatterns("/**")
		.excludePathPatterns(whiteList);
	}
	
	@Bean 
	public LoginCheckInterceptor loginCheckInterceptor() {
		return new LoginCheckInterceptor();
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
	
	
	
	
}
