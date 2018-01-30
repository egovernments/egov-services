package org.egov;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EgovPersistApplication {

	@Autowired
	public ResourceLoader resourceLoader;
	
	/*@Value("egov.persist.yaml.path")
	public String yamlPath;*/
	
	@Value("${egov.persist.yml.repo.path}")
	public String ymlRepoPaths;
	
	
	public static void main(String[] args) {
		SpringApplication.run(EgovPersistApplication.class, args);
	}
	
	@PostConstruct
	@Bean
	public TopicMap loadYaml() {
		TopicMap topicMap = new TopicMap();
		Map<String, Mapping> mappingsMap = new HashMap<>();

		log.info("EgovPersistApplication loadYaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Service service = null;
		
			try{
				List<String> ymlUrlS = Arrays.asList(ymlRepoPaths.split(","));
				for(String yamlLocation : ymlUrlS){
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
						int i=0;
						for(Mapping mapping: (service.getServiceMaps().getMappings())){
							
							mappingsMap.put(mapping.getFromTopic(),service.getServiceMaps().getMappings().get(i++));
						}
						
					} else { //  if(yamlLocation.startsWith("file://")){
						log.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource("classpath:"+yamlLocation);
							File file = resource.getFile();
							try{
								service = mapper.readValue(file, Service.class);
							 } catch(Exception e) {
									log.error("Exception while fetching service map for: "+yamlLocation);
									continue;
							}
							log.info("Parsed to object: "+service);
							int i=0;
							for(Mapping mapping: (service.getServiceMaps().getMappings())){
								mappingsMap.put(mapping.getFromTopic(),service.getServiceMaps().getMappings().get(i++));
							}
					}
				}
			}catch(Exception e){
				log.error("Exception while loading yaml files: ",e);
			}
		
		topicMap.setTopicMap(mappingsMap);
		log.info("topicMap:"+topicMap);
		log.info("mappingsMap.size():"+mappingsMap.size());
		
		return topicMap;
	}
}
