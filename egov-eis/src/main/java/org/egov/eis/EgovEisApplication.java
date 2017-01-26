package org.egov.eis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.egov.eis")
public class EgovEisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgovEisApplication.class, args);
	}
}
