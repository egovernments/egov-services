package org.egov.infra.persist;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.egov.infra.persist.web.contract.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EgovPersistApplication {

	@Autowired
	public ResourceLoader resourceLoader;
	
	@Value("egov.persist.yaml.path")
	public String yamlPath;
	
	public static void main(String[] args) {
		SpringApplication.run(EgovPersistApplication.class, args);
	}

	@PostConstruct
	@Bean
	public Service loadYaml() {
		System.out.println("EgovPersistApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
	
	/*@PostConstruct
	@Bean
	public Service loadYaml() {
		System.out.println("EgovPersistApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		
		Service service = null;
		try {
			  URL oracle = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/citizen/citizen-persister/src/main/resources/application.yml");
			  service = mapper.readValue(new InputStreamReader(oracle.openStream()), Service.class);
			  log.info("loadYaml service: " + service.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}*/
}
