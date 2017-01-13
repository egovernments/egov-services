package org.egov.pgr.web;

import javax.annotation.PostConstruct;

import org.egov.pgr.web.indexing.controller.IndexingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	@Autowired
	private IndexingController indexingController;

	@PostConstruct
	public void listen() {
		indexingController.savedRequestsReceiver();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}