package org.egov.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@SpringBootApplication
@EnableWebMvc 
public class PtPropertyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtPropertyApplication.class, args);

	}


}