package org.egov.infra.mdms.service;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class MDMSService {
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${egov.kafka.topics.reload}")
	private String reloadTopic;
	
	public Map<String, Map<String, JSONArray>> searchMaster(MdmsCriteriaReq mdmsCriteriaReq) {
		Map<String, Map<String, JSONArray>> responseMap = new HashMap<>();
		Map<String, Map<String, Map<String, JSONArray>>> tenantIdMap = MDMSApplicationRunnerImpl.getTenantMap();
		Map<String, Map<String, JSONArray>> moduleMap = tenantIdMap.get(mdmsCriteriaReq.getMdmsCriteria().getTenantId());

		if (moduleMap == null)
			throw new CustomException("Invalid_tenantId.MdmsCriteria.tenantId", "Invalid Tenant Id");

		List<ModuleDetail> moduleDetails = mdmsCriteriaReq.getMdmsCriteria().getModuleDetails();
		
		for(ModuleDetail moduleDetail : moduleDetails) {
			List<MasterDetail> masterDetails = moduleDetail.getMasterDetails();
			Map<String, JSONArray> masters = moduleMap.get(moduleDetail.getModuleName());
			if(masters == null) 
			continue;
			
			Map<String, JSONArray> finalMasterMap = new HashMap<>();
			for(MasterDetail masterDetail : masterDetails) {
				JSONArray masterData = masters.get(masterDetail.getName());
				
				if(masterData == null) 
				continue;
				
				if (masterDetail.getFilter() != null)
					masterData = filterMaster(masterData, masterDetail.getFilter());
				
				finalMasterMap.put(masterDetail.getName(), masterData);
			}
			responseMap.put(moduleDetail.getModuleName(), finalMasterMap);
		}
		return responseMap;
	}
	
	public JSONArray filterMaster(JSONArray masters, String filterExp) {
		JSONArray filteredMasters = JsonPath.read(masters, filterExp);
		return filteredMasters;
	}
	
	public void updateCache(String path, String tenantId) {

		ObjectMapper jsonReader = new ObjectMapper();
		
			try {
				URL jsonFileData = new URL(path);
				Map<String, Object> map = jsonReader.readValue(new InputStreamReader(jsonFileData.openStream()), Map.class);
				kafkaTemplate.send(reloadTopic, map);
			} catch (Exception e) {
				e.printStackTrace();
				throw new CustomException("mdms_invalid_file_path","invalid file path");
			}


	}
	
	public void reloadObj(Map<String, Object> map) {
		kafkaTemplate.send(reloadTopic, map);
	}
}
