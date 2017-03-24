package org.egov.lams.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationApplication extends Thread{
	
	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}
}
