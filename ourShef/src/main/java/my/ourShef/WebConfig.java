package my.ourShef;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import my.ourShef.filter.LogFilter;
import my.ourShef.filter.LoginCheckFilter;

/*
 * 필터를 스프링부트를 통해 내장 WAS에 등록합니다.
 */
@Configuration
public class WebConfig {

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
		
		return filterRegistrationBean;	
	}
	
}
