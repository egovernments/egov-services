package org.egov;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MDMSApplicationRunnerImpl {

	@Autowired
	public ResourceLoader resourceLoader;

	private static Map<String, List<Object>> tenantMap = new HashMap<>();

	@PostConstruct
	public void run() {
		try {
			log.info("Reading yaml files......");
			readDirectory("/home/user/Desktop/config/test");
			System.out.println("tenantMap:" + tenantMap);
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
				if (name.contains("yml") || name.contains("yaml")) {
					log.info("Reading yaml file....");
					try {
						Map<String, Object> obj = yamlReader.readValue(file, Map.class);
						filterMaster(obj);
						System.out.println("yaml obj:" + obj);

					} catch (Exception e) {
						log.error("Exception while fetching service map for: ");
						continue;
					}
				} else if (name.contains("json")) {
					log.info("Reading json file....");

					try {
						Map<String, Object> jsonStr = jsonReader.readValue(file, Map.class);
						filterMaster(jsonStr);
						System.out.println(jsonStr);
					} catch (JsonGenerationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else if (listOfFiles[i].isDirectory()) {
				log.info("Directory " + listOfFiles[i].getName());
				readDirectory(listOfFiles[i].getAbsolutePath());
			}
		}

	}

	private void filterMaster(Map<String, Object> map) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Object> list = null;
			if (!tenantMap.containsKey(map.get("tenantId").toString())) {
				list = new ArrayList<>();
				list.add(map);
			} else {
				List<Object> list1 = tenantMap.get(map.get("tenantId").toString());
				list1.add(map);
				list = list1;
			}

			tenantMap.put(map.get("tenantId").toString(), list);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Map<String, List<Object>> getTenantMap(){
		return tenantMap;
	}
	
}
