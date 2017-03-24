package org.egov.lams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LamsPersistApplication extends Thread{
	
	public static void main(String[] args) {
		SpringApplication.run(LamsPersistApplication.class, args);
	}
}
