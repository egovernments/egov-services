package org.egov.citizen;

import java.io.File;

import javax.annotation.PostConstruct;

import org.egov.citizen.model.ServiceCollection;
import org.egov.citizen.model.ServiceConfigs;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@Import({TracerConfiguration.class})
public class EgovCitizenApplication {

	@Autowired
	public ResourceLoader resourceLoader;
	
	public static void main(String[] args) {
		SpringApplication.run(EgovCitizenApplication.class, args);
	}

	@PostConstruct
	@Bean
	public ServiceCollection loadYaml() {
		System.out.println("EgovPersistApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ServiceCollection serviceDefination = null;
		try {
			  Resource resource = resourceLoader.getResource("classpath:ServiceDefination.yml"); 
			  File file = resource.getFile(); 
			  serviceDefination = mapper.readValue(file, ServiceCollection.class);
			  log.info("loadYaml service: " + serviceDefination.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceDefination;
	}
	
	@PostConstruct
	@Bean
	public ServiceConfigs loadServiceConfigYaml() {
		System.out.println("EgovPersistApplication ServiceConfigLoadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ServiceConfigs serviceConfig = null;
		try {
			  Resource resource = resourceLoader.getResource("classpath:ServiceConfigs.yml"); 
			  File file = resource.getFile(); 
			  serviceConfig = mapper.readValue(file, ServiceConfigs.class);
			  log.info("loadYaml service: " + serviceConfig.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceConfig;
	}
}
