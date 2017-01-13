package org.egov.notification.web;

import javax.annotation.PostConstruct;

import org.egov.notification.web.consumer.NotificationConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Autowired
	private NotificationConsumer notificationConsumer;

	@PostConstruct
	public void listen() {
		notificationConsumer.consume();
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}
}