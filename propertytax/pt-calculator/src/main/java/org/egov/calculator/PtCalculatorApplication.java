package org.egov.calculator;

import org.egov.calculator.config.PropertiesManager;
import org.egov.models.ResponseInfoFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Description : This is initialization class for pt-calculator module
 * 
 * @author Narendra
 *
 */
@SpringBootApplication
@Import({TracerConfiguration.class})
@EnableWebMvc
public class PtCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtCalculatorApplication.class, args);
	}

	@Bean
	public ResponseInfoFactory responseInfoFactory() {
		return new ResponseInfoFactory();
	}
	
	@Bean
	public PropertiesManager getPropertiesManager() {
		return new PropertiesManager();
	}

}
