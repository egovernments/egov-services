package org.egov.infra.mdms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.infra.mdms.model.MasterDetail;
import org.egov.infra.mdms.model.MdmsCriteriaReq;
import org.egov.infra.mdms.model.ModuleDetail;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MDMSService {

	public Map<String, List<Map<String, JSONArray>>> getMaster(MdmsCriteriaReq mdmsCriteriaReq) {
		Map<String, List<Object>> tenantIdMap = MDMSApplicationRunnerImpl.getTenantMap();
		List<Object> list = tenantIdMap.get(mdmsCriteriaReq.getMdmsCriteria().getTenantId());
		ObjectMapper objectMapper = new ObjectMapper();
		List<ModuleDetail> moduleDetails = mdmsCriteriaReq.getMdmsCriteria().getModuleDetails();
		// String masterDataJson = null;
		String tenantJsonList = null;
		Map<String, List<Map<String, JSONArray>>> moduleMap = new HashMap<>(); 
		
		try {
			// masterDataJson = objectMapper.writeValueAsString(tenantIdMap);
			tenantJsonList = objectMapper.writeValueAsString(list);
			log.info("MDMSService tenantJsonList:" + tenantJsonList);
			for(ModuleDetail moduleDetail : moduleDetails) {
				List<Map<String, JSONArray>> response = new ArrayList<>();
				String moduleFilterJsonPath = "$[?(@.moduleName==".concat("\"").
						concat(moduleDetail.getModuleName()).concat("\"").concat(")]");
				log.info("moduleFilterJsonPath:"+ moduleFilterJsonPath);
				JSONArray mastersOfModule = JsonPath.read(tenantJsonList, moduleFilterJsonPath);
				
				for(MasterDetail masterDetail : moduleDetail.getMasterDetails()) {
					Map<String, JSONArray> resMap = new HashMap<String, JSONArray>();
					//String moduleJson = objectMapper.writeValueAsString(list);
					JSONArray masters = JsonPath.read(mastersOfModule, "$.*".concat(masterDetail.getName()).concat(".*"));
				//	JSONArray filteredMasters = JsonPath.read(masters, jsonPath, filters)
					log.info("masters:"+masters);
					resMap.put(masterDetail.getName(), masters);
					response.add(resMap);
				}
				
				moduleMap.put(moduleDetail.getModuleName(), response);
				System.out.println("moduleMap:"+moduleMap);
				//response.clear();
			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		return moduleMap;
	}

}
