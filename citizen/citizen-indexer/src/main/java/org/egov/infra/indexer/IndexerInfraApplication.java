package org.egov.infra.indexer;


import java.io.File;

import javax.annotation.PostConstruct;

import org.egov.infra.indexer.web.contract.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j 

/**
 * This is the Application file for indexer.
 * @author ranjeetvimal
 * @version 1.0
 */

public class IndexerInfraApplication
{
	
	@Bean
	public RestTemplate restTemplate() {
		// TODO Auto-generated method stub
		return new RestTemplate();
	}
    
	@Autowired
	public ResourceLoader resourceLoader;
	
	public static void main(String[] args) {
		SpringApplication.run(IndexerInfraApplication.class, args);
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
