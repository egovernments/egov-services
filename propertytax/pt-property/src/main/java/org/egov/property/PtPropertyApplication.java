package org.egov.property;

import org.egov.models.ResponseInfoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@SpringBootApplication
@EnableWebMvc 
public class PtPropertyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtPropertyApplication.class, args);

	}
	
	@Bean
	public ResponseInfoFactory responseInfoFactory(){
		return new ResponseInfoFactory();
	}


}