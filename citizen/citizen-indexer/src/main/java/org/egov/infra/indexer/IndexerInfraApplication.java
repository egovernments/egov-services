package org.egov.infra.indexer;


import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.egov.infra.indexer.web.contract.Service;
import org.egov.tracer.config.TracerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * This is the Application file for indexer.
 * @author ranjeetvimal
 * @version 1.0
 */

@SpringBootApplication
@Import({TracerConfiguration.class})
public class IndexerInfraApplication
{
	
	/* @Bean
 	   public RestTemplate restTemplate() {
		// TODO Auto-generated method stub
		return new RestTemplate();
	} */
    
	public static final Logger logger = LoggerFactory.getLogger(IndexerInfraApplication.class);

	@Autowired
	public ResourceLoader resourceLoader;
	
	public static void main(String[] args) {
		SpringApplication.run(IndexerInfraApplication.class, args);
	}

	@PostConstruct
	@Bean
	public Service loadYaml() {
		logger.info("IndexerInfraApplication starting......");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Service service = null;
		try {
			logger.info("Reading application.yml file......");
			URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/citizen/citizen-indexer/src/main/resources/application.yml");
			service = mapper.readValue(new InputStreamReader(url.openStream()), Service.class);
			logger.info("Yaml to service: " + service.toString());
		} catch (Exception e) {
			logger.error("Exception while loading the yaml file: ",e);
		}
		return service;
	}
}
