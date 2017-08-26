package org.egov.citizen;

import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.egov.citizen.model.ServiceCollection;
import org.egov.citizen.model.ServiceConfigs;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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
			URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/citizen/citizen-services/src/main/resources/ServiceDefination.yml");
			serviceDefination = mapper.readValue(new InputStreamReader(url.openStream()), ServiceCollection.class);
			log.info("loadYamlserviceconfig service: " + serviceDefination.toString());

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
			URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/citizen/citizen-services/src/main/resources/ServiceConfigs.yml");
			serviceConfig = mapper.readValue(new InputStreamReader(url.openStream()), ServiceConfigs.class);
			log.info("loadYaml service: " + serviceConfig.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceConfig;
	}
}
