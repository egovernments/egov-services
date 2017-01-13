package org.egov.pgr.web;

import javax.annotation.PostConstruct;

import org.egov.pgr.web.persist.controller.PersistController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Autowired
	private PersistController persistController;

	@PostConstruct
	public void listen() {
		persistController.assignedRequestsReceiver();
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}
}