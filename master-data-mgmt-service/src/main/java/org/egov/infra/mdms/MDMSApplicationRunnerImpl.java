                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  package org.egov.infra.mdms;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class MDMSApplicationRunnerImpl implements ApplicationRunner {

	@Autowired
	public static ResourceLoader resourceLoader;
	
	@Value("${egov.mdms.configs.file.path}")
	private String configFiles;
	
	public static final Logger logger = LoggerFactory.getLogger(MDMSApplicationRunnerImpl.class);

    public static volatile ConcurrentHashMap<String, Map<String, String>> configMap  = new ConcurrentHashMap<>();
	
    @Override
    public void run(final ApplicationArguments arg0) throws Exception {
    	ConcurrentHashMap<String, Map<String, String>> configMap = new ConcurrentHashMap<>();
    	try {
				logger.info("Reading yaml files......");			
			    readDirectory("/home/vishal/WaterCharge/egov-services/master-data-mgmt-service/src/main/resources/config-files");			
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
    	
    	this.configMap = configMap;
    }
    
    public ConcurrentHashMap<String, Map<String, String>> getConfigMap(){
    	return this.configMap;
    }
    
    public void readDirectory(String path){
    	List<String> files = new ArrayList<>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        logger.info("File " + listOfFiles[i].getName());
	        files.add(listOfFiles[i].getName());
	      } else if (listOfFiles[i].isDirectory()) {
	    	  logger.info("Directory " + listOfFiles[i].getName());
	    	  readDirectory(listOfFiles[i].getAbsolutePath());
	      }
	    }
    }
}
