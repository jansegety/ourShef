package my.ourShef;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory>{

	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		
		
		ErrorPage errorPageFileSizeLimitExceeded = new ErrorPage(MaxUploadSizeExceededException.class, "/error-page/fileSizeLimitExceeded");
		
		factory.addErrorPages(errorPageFileSizeLimitExceeded);
		
	}

	
}
