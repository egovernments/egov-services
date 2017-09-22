package org.egov.infra.indexer;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.infra.indexer.web.contract.Service;
import org.egov.tracer.config.TracerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
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
@PropertySource("classpath:application.properties")
public class IndexerInfraApplication
{
	    
	public static final Logger logger = LoggerFactory.getLogger(IndexerInfraApplication.class);

	@Autowired
	public ResourceLoader resourceLoader;
	
    @Autowired
    private static Environment env;
    
    @Autowired
    private static Map<String, Service> serviceMaps;
    
    @Value("${egov.indexer.file.path}")
    private String yamllistfile;
    
    public void setEnvironment(final Environment env) {
    	IndexerInfraApplication.env = env;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(IndexerInfraApplication.class, args);
	}    

	@PostConstruct
	@Bean
	public Map<String, Service> loadYaml() {
		Map<String, Service> serviceMap = new HashMap();
		logger.info("IndexerInfraApplication starting......");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Service service = null;
		try {
			logger.info("Reading yaml files......");
			URL url = new URL(yamllistfile);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			try{
				String yamlLocation;
				while(null != (yamlLocation = bufferedReader.readLine())){
					if(yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
						logger.info("Reading....: "+yamlLocation);
						URL yamlFile = new URL(yamlLocation);
						try{
						    service = mapper.readValue(new InputStreamReader(yamlFile.openStream()), Service.class);
						} catch(Exception e) {
							logger.error("Exception while fetching service map for: "+yamlLocation);
							continue;
						}
						serviceMap.put(service.getServiceMaps().getServiceName(), service);			
					} else if(yamlLocation.startsWith("file://")){
						logger.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource(yamlLocation);
							File file = resource.getFile();
							try{
								service = mapper.readValue(file, Service.class);
							 } catch(Exception e) {
									logger.error("Exception while fetching service map for: "+yamlLocation);
									continue;
							}
							serviceMap.put(service.getServiceMaps().getServiceName(), service);				
					}
				}
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
		logger.info("Read and parsed a total of: "+serviceMap.size()+" files");	
		} catch (Exception e) {
			logger.error("Exception while loading file containing yaml locations: ",e);
		}
		
		serviceMaps = serviceMap;
		return serviceMap;
	}
	
	public static Map<String, Service> getServiceMaps(){
		return serviceMaps;
	}
}
