package org.egov.pgr;

import javax.annotation.PostConstruct;

import org.egov.pgr.controller.ServiceRequestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PgrrestspringbootApplication {
	
	@Autowired
	private ServiceRequestController serviceRequestController;
	
	@PostConstruct
	public void listen() {
		serviceRequestController.assignedRequestsReceiver();
	}

	public static void main(String[] args) {
		SpringApplication.run(PgrrestspringbootApplication.class, args);
	}
}
