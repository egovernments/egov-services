package org.egov.infra.indexer;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.Services;
import org.egov.tracer.config.TracerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * This is the Application file for indexer.
 * @author ranjeetvimal
 * @version 1.0
 */

@SpringBootApplication
@Configuration
@PropertySource("classpath:application.properties")
public class IndexerInfraApplication
{
	    
	public static final Logger logger = LoggerFactory.getLogger(IndexerInfraApplication.class);

	@Autowired
	public static ResourceLoader resourceLoader;
	
    @Autowired
    private static Environment env;
    
    @Autowired
    private static Map<String, Mapping> mappingMaps;
    
    @Value("${egov.indexer.file.path}")
    private static String yamllistfile;
    
    public void setEnvironment(final Environment env) {
    	IndexerInfraApplication.env = env;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(IndexerInfraApplication.class, args);
	}    

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@PostConstruct
	@Bean
	public static Map<String, Mapping> loadYaml() {
		Map<String, Mapping> mappingsMap = new HashMap();

		logger.info("IndexerInfraApplication starting......");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Services service = null;
		try {
			logger.info("Reading yaml files......");
			URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/docs/indexerinfra/indexeryaml/indexeryamlfilelocationlistfile.txt");
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			try{
				String yamlLocation;
				while(null != (yamlLocation = bufferedReader.readLine())){
					if(yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
						logger.info("Reading....: "+yamlLocation);
						URL yamlFile = new URL(yamlLocation);
						try{
						    service = mapper.readValue(new InputStreamReader(yamlFile.openStream()), Services.class);
						} catch(Exception e) {
							logger.error("Exception while fetching service map for: "+yamlLocation+" = ",e);
							continue;
						}
						logger.info("Parsed to object: "+service);
						for(Mapping mapping: (service.getServiceMaps().getMappings())){
							mappingsMap.put(mapping.getTopic(), mapping);
						}
						
					} else if(yamlLocation.startsWith("file://")){
						logger.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource(yamlLocation);
							File file = resource.getFile();
							try{
								service = mapper.readValue(file, Services.class);
							 } catch(Exception e) {
									logger.error("Exception while fetching service map for: "+yamlLocation);
									continue;
							}
							logger.info("Parsed to object: "+service);
							for(Mapping mapping: (service.getServiceMaps().getMappings())){
								mappingsMap.put(mapping.getTopic(), mapping);
							}
					}
				}
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
		} catch (Exception e) {
			logger.error("Exception while loading file containing yaml locations: ",e);
		}
		
		mappingMaps = mappingsMap;
		return mappingsMap;
	}
	
	public static Map<String, Mapping> getMappingMaps(){
		return mappingMaps;
	}
	
}
