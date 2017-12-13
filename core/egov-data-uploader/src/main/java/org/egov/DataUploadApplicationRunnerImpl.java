package org.egov;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.egov.dataupload.model.UploadDefinition;
import org.egov.dataupload.model.UploadDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Component
@Order(1)
public class DataUploadApplicationRunnerImpl implements ApplicationRunner {

	@Autowired
	public static ResourceLoader resourceLoader;
	        
    @Autowired
    private static Environment env;
    
    @Value("${upload.yaml.path}")
    private String yamllist;
    
    public static ConcurrentHashMap<String, UploadDefinition> uploadDefinitionMap  = new ConcurrentHashMap<>();

	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadApplicationRunnerImpl.class);
	
    @Override
    public void run(final ApplicationArguments arg0) throws Exception {
    	try {
				logger.info("Reading yaml files......");			
			    readFiles();			
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
    }
    
	public DataUploadApplicationRunnerImpl(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
    }
       
    public void readFiles(){
    	ConcurrentHashMap<String, UploadDefinition> map  = new ConcurrentHashMap<>();
    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		UploadDefinitions uploadDefinitions = null;
		try{
				List<String> ymlUrlS = Arrays.asList(yamllist.split(","));
				if(0 == ymlUrlS.size()){
					ymlUrlS.add(yamllist);
				}
				for(String yamlLocation : ymlUrlS){
					if(yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
						logger.info("Reading....: "+yamlLocation);
						URL yamlFile = new URL(yamlLocation);
						try{
							uploadDefinitions = mapper.readValue(new InputStreamReader(yamlFile.openStream()), UploadDefinitions.class);
						} catch(Exception e) {
							logger.error("Exception while fetching upload definitions for: "+yamlLocation+" = ",e);
							continue;
						}
						logger.info("Parsed to object: "+uploadDefinitions.toString());
						map.put(uploadDefinitions.getUploadDefinition().getModuleName(), 
								uploadDefinitions.getUploadDefinition());
						
					} else if(yamlLocation.startsWith("file://")){
						logger.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource(yamlLocation);
							File file = resource.getFile();
							try{
								uploadDefinitions = mapper.readValue(file, UploadDefinitions.class);
							 } catch(Exception e) {
									logger.error("Exception while fetching upload definitions for: "+yamlLocation+" = ",e);
									continue;
							}
							logger.info("Parsed to object: "+uploadDefinitions.toString());
							map.put(uploadDefinitions.getUploadDefinition().getModuleName(), 
									uploadDefinitions.getUploadDefinition());
					}
				}
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
		uploadDefinitionMap = map;
    }
   

	public ConcurrentHashMap<String, UploadDefinition> getUploadDefinitionMap(){
		return uploadDefinitionMap;
	}
}
