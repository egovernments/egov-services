package org.egov.infra.indexer;


import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.assertj.core.api.UrlAssert;
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
		System.out.println("EgovIndexrApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Service service = null;
		try {
			URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/citizen/citizen-indexer/src/main/resources/application.yml");
			
			service = mapper.readValue(new InputStreamReader(url.openStream()), Service.class);
			  
			log.info("loadYaml service: " + service.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}
}
