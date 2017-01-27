package org.egov.pgr.web;

import javax.annotation.PostConstruct;

import org.egov.pgr.web.locationassignment.controller.LocationAssignmentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	@Autowired
	private LocationAssignmentController locationAssignmentController;

	@PostConstruct
	public void listen() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				locationAssignmentController.validatedRequestsReceiver();
			}
		}).start();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}