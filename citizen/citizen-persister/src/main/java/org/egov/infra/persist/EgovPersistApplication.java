package org.egov.infra.persist;

import java.io.File;

import javax.annotation.PostConstruct;

import org.egov.infra.persist.web.contract.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EgovPersistApplication {

	@Autowired
	public ResourceLoader resourceLoader;
	
	public static void main(String[] args) {
		SpringApplication.run(EgovPersistApplication.class, args);
	}

	@PostConstruct
	@Bean
	public Service loadYaml() {
		System.out.println("EgovPersistApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Service service = null;
		try {
			  Resource resource = resourceLoader.getResource("classpath:application.yml"); 
			  File file = resource.getFile(); 
			  service = mapper.readValue(file, Service.class);
			  log.info("loadYaml service: " + service.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}
}
