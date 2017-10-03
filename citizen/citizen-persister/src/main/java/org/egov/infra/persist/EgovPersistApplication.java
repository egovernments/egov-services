package org.egov.infra.persist;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.infra.persist.web.contract.Mapping;
import org.egov.infra.persist.web.contract.Service;
import org.egov.infra.persist.web.contract.TopicMap;
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
	public TopicMap loadYaml() {
		TopicMap topicMap = new TopicMap();
		Map<String, Service> mappingsMap = new HashMap<>();

		log.info("EgovPersistApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Service service = null;
		try {
			log.info("Reading yaml files......");
			URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/docs/persist-infra/persist-conf-location/persist-conf-loctions.txt");
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			try{
				String yamlLocation;
				while(null != (yamlLocation = bufferedReader.readLine())){
					if(yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
						log.info("Reading....: "+yamlLocation);
						URL yamlFile = new URL(yamlLocation);
						try{
						    service = mapper.readValue(new InputStreamReader(yamlFile.openStream()), Service.class);
						} catch(Exception e) {
							log.error("Exception while fetching service map for: "+yamlLocation+" = ",e);
							continue;
						}
						log.info("Parsed to object: "+service);
						for(Mapping mapping: (service.getServiceMaps().getMappings())){
							mappingsMap.put(mapping.getFromTopic(),service);
						}
						
					} else if(yamlLocation.startsWith("file://")){
						log.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource(yamlLocation);
							File file = resource.getFile();
							try{
								service = mapper.readValue(file, Service.class);
							 } catch(Exception e) {
									log.error("Exception while fetching service map for: "+yamlLocation);
									continue;
							}
							log.info("Parsed to object: "+service);
							for(Mapping mapping: (service.getServiceMaps().getMappings())){
								mappingsMap.put(mapping.getFromTopic(),service);
							}
					}
				}
			}catch(Exception e){
				log.error("Exception while loading yaml files: ",e);
			}
		} catch (Exception e) {
			log.error("Exception while loading file containing yaml locations: ",e);
		}
		
		topicMap.setTopicMap(mappingsMap);
		log.info("topicMap:"+topicMap);
		return topicMap;
	}
	
	
	/*@PostConstruct
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
	}*/
	
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
