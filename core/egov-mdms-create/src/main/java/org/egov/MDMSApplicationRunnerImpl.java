package org.egov;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MDMSApplicationRunnerImpl implements ApplicationRunner {

	@Autowired
	public ResourceLoader resourceLoader;
	
	@Value("${egov.mdms.conf.path}")
	public String mdmsFileDirectory;
	
	private static Map<String, String> filePathMap = new HashMap<>();

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			log.info("Reading data files from: "+mdmsFileDirectory);
			readDirectory(mdmsFileDirectory);
		} catch (Exception e) {
			log.error("Exception while loading yaml files: ", e);
		}

	}

	public void readDirectory(String path) {
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		ObjectMapper jsonReader = new ObjectMapper();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				log.info("File " + listOfFiles[i].getName());
				File file = listOfFiles[i];
				String name = file.getName();
				String [] fileName = name.split("[.]");
				if(fileName[fileName.length - 1].equals("yml") ||
						fileName[fileName.length - 1].equals("yaml")){
					log.info("Reading yaml file....:- "+name);
					try {
						Map<String, Object> obj = yamlReader.readValue(file, Map.class);
						buildFilePathMap(obj, file.getName());
					} catch (Exception e) {
						log.error("Exception while fetching service map for: ");
						continue;
					}
				} else if (fileName[fileName.length - 1].equals("json")) {
					log.info("Reading json file....:- "+name);
					try {
						Map<String, Object> jsonStr = jsonReader.readValue(file, Map.class);
						buildFilePathMap(jsonStr, file.getName());
					} catch (Exception e) {
						log.error("Exception while fetching service map for: ");
						continue;
					}
				} else {
					log.info("file is not of a valid type please change and retry");
					log.info("Note: file can either be .yml/.yaml or .json");
				}

			} else if (listOfFiles[i].isDirectory()) {
				log.info("Directory " + listOfFiles[i].getName());
				readDirectory(listOfFiles[i].getAbsolutePath());
			}
		}
		
		log.info("filePathMap: "+filePathMap);
	}		
	private void buildFilePathMap(Map<String, Object> map, String filePath){
		StringBuilder key = new StringBuilder();
		key.append(map.get("tenantId")).append("-").append(map.get("moduleName"));
		
		filePathMap.put(key.toString(), filePath);
	}

	
	public static Map<String, String> getFilePathMap(){
		return filePathMap;
	
	}
	
}
