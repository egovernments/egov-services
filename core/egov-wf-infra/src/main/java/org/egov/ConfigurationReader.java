package org.egov;

import java.io.File;

import javax.annotation.PostConstruct;

import org.egov.wf.model.ServiceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConfigurationReader {

	@Autowired
	public ResourceLoader resourceLoader;
	
	@PostConstruct
	@Bean
	public ServiceMap loadServiceConfigurationYaml() {
		System.out.println("EgovPersistApplication ServiceConfigLoadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ServiceMap service = null;
		try {
			  Resource resource = resourceLoader.getResource("classpath:ServicesConfiguration.yml"); 
			  File file = resource.getFile(); 
			  service = mapper.readValue(file, ServiceMap.class);
			  log.info("loadYaml service: " + service.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}
	
}
