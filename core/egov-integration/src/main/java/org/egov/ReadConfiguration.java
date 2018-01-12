package org.egov;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.filter.model.MasterDetail;
import org.egov.filter.model.Mdms;
import org.egov.filter.model.MdmsMap;
import org.egov.filter.model.ServiceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReadConfiguration {

	@Autowired
	public ResourceLoader resourceLoader;
	
	private static Map<String, Map<String, MasterDetail>> moduleMap;
	
	@Value("${egov.service.config.path}")
	private String serviceConfigPath;
	
	@Value("${egov.mdms.config.path}")
	private String mdmsConfigPath;
	
	@PostConstruct
	@Bean
	public ServiceMap loadServiceConfigurationYaml() {
		System.out.println(" Translator Service ReadConfiguration");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ServiceMap serviceMap = null;
		
		try {
			  
			 /* Resource resource = resourceLoader.getResource("classpath:ServicesConfiguration.yml"); 
			  File file = resource.getFile(); 
			  serviceMap = mapper.readValue(file, ServiceMap.class);*/
			  
			  URL serviceConfigUrl = new URL(serviceConfigPath);
			  serviceMap = mapper.readValue(new InputStreamReader(serviceConfigUrl.openStream()), ServiceMap.class);
			  log.info("loadYaml service: " + serviceMap.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceMap;
	}
	
	@PostConstruct
	public void loadMdmsConfig() {
		System.out.println("loading Mdms configuration");
		
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		MdmsMap mdmsMap = null;
		try {
			
			  /*Resource resource = resourceLoader.getResource("classpath:MdmsConfig.yml"); 
			  File file = resource.getFile(); 
			  mdmsMap = mapper.readValue(file, MdmsMap.class);*/
			  URL mdmsConfigUrl = new URL(mdmsConfigPath);
			  mdmsMap = mapper.readValue(new InputStreamReader(mdmsConfigUrl.openStream()), MdmsMap.class);
			  log.info("loadYaml mdms: " + mdmsMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		moduleMap = getMdmsMasterMap(mdmsMap);
	}
	
	private Map<String, Map<String, MasterDetail>> getMdmsMasterMap(MdmsMap mdmsMap) {
		List<Mdms> mdmsList = mdmsMap.getMdms();
		Map<String, Map<String, MasterDetail>> moduleMap = new HashMap<>();
		for(Mdms mdms : mdmsList) {
			Map<String, MasterDetail> masterMap = new HashMap<>();
			List<MasterDetail> masterDetails = mdms.getMasterDetails();
			
			for(MasterDetail masterDetail : masterDetails) {
				masterMap.put(masterDetail.getMasterName(), masterDetail);
			}
			
			moduleMap.put(mdms.getModuleName(), masterMap);
		}
		return moduleMap;
	}
	
	
	public static Map<String, Map<String, MasterDetail>> getMdmsConfigMap(){
		return moduleMap;
	}
}
