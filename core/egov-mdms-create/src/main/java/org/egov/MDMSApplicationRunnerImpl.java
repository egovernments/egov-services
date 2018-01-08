package org.egov;

import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MDMSApplicationRunnerImpl implements ApplicationRunner {

	@Autowired
	public ResourceLoader resourceLoader;

	@Value("${egov.mdms.conf.path}")
	public String mdmsFileDirectory;

	@Value("${masters.config.url}")
	public String masterConfigUrl;

	private static Map<String, String> filePathMap = new HashMap<>();

	private static Map<String, Map<String, Object>> masterConfigMap = new HashMap<>();

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			log.info("Reading data files from: " + mdmsFileDirectory);
			readDirectory(mdmsFileDirectory);
			readMdmsConfigFiles(masterConfigUrl);
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
				String[] fileName = name.split("[.]");
				if (fileName[fileName.length - 1].equals("yml") || fileName[fileName.length - 1].equals("yaml")) {
					log.info("Reading yaml file....:- " + name);
					try {
						Map<String, Object> obj = yamlReader.readValue(file, Map.class);
						buildFilePathMap(obj, file.getName());
					} catch (Exception e) {
						log.error("Exception while fetching service map for: ");
						continue;
					}
				} else if (fileName[fileName.length - 1].equals("json")) {
					log.info("Reading json file....:- " + name);
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

		log.info("filePathMap: " + filePathMap);
	}

	private void buildFilePathMap(Map<String, Object> map, String filePath) {
		Set<String> set = map.keySet();
		String masterName = set.stream()
				.filter(line -> !"tenantId".equals(line) && !"moduleName".equals(line) && !"mdms-config".equals(line))
				.findAny().get();
		StringBuilder key = new StringBuilder();
		key.append(map.get("tenantId")).append("-").append(map.get("moduleName")).append("-").append(masterName);
		filePathMap.put(key.toString(), filePath);
	}

	public static Map<String, String> getFilePathMap() {
		return filePathMap;
	}

	/*	*//**
			 * Preparing StateLevel Map By configured (state.level.masters) in
			 * application properties.
			 * 
			 * @param stateLevelMasters
			 *//*
			 * public void getStateLevelMap(String stateLevelMasters) { if
			 * (!StringUtils.isEmpty(stateLevelMasters)) { if
			 * (stateLevelMasters.contains(",")) { List<String> list =
			 * Arrays.asList(stateLevelMasters.split("\\,")); for (String key :
			 * list) { String[] array = key.split("\\."); if
			 * (!stateLevelMastermap.containsKey(array[0])) { List<String>
			 * values = new ArrayList<String>(); values.add(array[1]);
			 * stateLevelMastermap.put(array[0], values); } else { List<String>
			 * values = stateLevelMastermap.get(array[0]); values.add(array[1]);
			 * stateLevelMastermap.put(array[0], values); } } } else { //
			 * Assuming Data Always Given like ModuleName.MasterName String[]
			 * array = stateLevelMasters.split("\\."); List<String> values = new
			 * ArrayList<String>(); values.add(array[1]);
			 * stateLevelMastermap.put(array[0], values); } } }
			 */

	public void readMdmsConfigFiles(String masterConfigUrl) {
		ObjectMapper jsonReader = new ObjectMapper();

		log.info("GitHub Url TO Fetch MasterConfigs: " + masterConfigUrl);
		Map file = null;
		URL yamlFile = null;
		try {
			yamlFile = new URL(masterConfigUrl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			log.error("Exception while fetching service map for: " + e1.getMessage());
		}
		try {
			file = jsonReader.readValue(new InputStreamReader(yamlFile.openStream()), Map.class);
		} catch (Exception e) {
			log.error("Exception while fetching service map for: " + e.getMessage());
		}

		masterConfigMap = file;
		log.debug("the Master config Map : " + masterConfigMap);

		// File file = new
		// File(getClass().getClassLoader().getResource(baseFoderPath).getFile());
		/*
		 * String name = file.getName(); String[] fileName = name.split("[.]");
		 * if (fileName[fileName.length - 1].equals("json")) {
		 * log.debug("Reading json file....:- " + name); try { Map<String,
		 * Object> jsonMap = jsonReader.convertValue(file, Map.class);
		 * prepareMasterConfigMap(jsonMap); log.debug("json str:" + jsonMap); }
		 * catch (JsonGenerationException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (JsonMappingException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (Exception ex) { ex.printStackTrace(); } } else {
		 * log.info("file is not of a valid type please change and retry");
		 * log.info("Note: file can either be .yml/.yaml or .json");
		 * 
		 * }
		 */
	}

	@SuppressWarnings("unchecked")
	public void prepareMasterConfigMap(Map<String, Object> map) {
		ObjectMapper objectMapper = new ObjectMapper();

		Set<String> moduleKeys = map.keySet();
		Iterator<String> moduleKeyIterator = moduleKeys.iterator();
		String masterName = null;
		Map<String, Object> masterDataJsonArray = null;
		while (moduleKeyIterator.hasNext()) {
			masterName = moduleKeyIterator.next();

			try {
				masterDataJsonArray = JsonPath.read(objectMapper.writeValueAsString(map.get(masterName)), "$");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			masterConfigMap.put(masterName, masterDataJsonArray);
		}
		log.info("MasterConfigMap: " + masterConfigMap);
	}

	public static Map<String, Map<String, Object>> getMasterConfigMap() {
		return masterConfigMap;
	}

}
