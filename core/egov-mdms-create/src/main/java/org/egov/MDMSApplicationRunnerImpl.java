package org.egov;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

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
	
	@Value("${state.level.masters}")
	public String stateLevelMasters;
	
	private static Map<String, String> filePathMap = new HashMap<>();
	
	private static Map<String, List<String>> stateLevelMastermap = new HashMap<>();

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			log.info("Reading data files from: "+mdmsFileDirectory);
			readDirectory(mdmsFileDirectory);
			getStateLevelMap(stateLevelMasters);
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
	
	/**
	 * Preparing StateLevel Map By configured (state.level.masters) in
	 * application properties.
	 * 
	 * @param stateLevelMasters
	 */
	public void getStateLevelMap(String stateLevelMasters) {
		if (!StringUtils.isEmpty(stateLevelMasters)) {
			if (stateLevelMasters.contains(",")) {
				List<String> list = Arrays.asList(stateLevelMasters.split("\\,"));
				for (String key : list) {
					String[] array = key.split("\\.");
					if (!stateLevelMastermap.containsKey(array[0])) {
						List<String> values = new ArrayList<String>();
						values.add(array[1]);
						stateLevelMastermap.put(array[0], values);
					} else {
						List<String> values = stateLevelMastermap.get(array[0]);
						values.add(array[1]);
						stateLevelMastermap.put(array[0], values);
					}
				}
			} else {
				// Assuming Data Always Given like ModuleName.MasterName
				String[] array = stateLevelMasters.split("\\.");
				List<String> values = new ArrayList<String>();
				values.add(array[1]);
				stateLevelMastermap.put(array[0], values);
			}
		}
	}

	public static Map<String, List<String>> getStateWideMastersMap() {
		return stateLevelMastermap;
	}
	
}
