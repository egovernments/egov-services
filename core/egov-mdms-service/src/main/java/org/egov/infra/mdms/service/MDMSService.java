package org.egov.infra.mdms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class MDMSService {

	public Map<String, Map<String, JSONArray>> getMaster(MdmsCriteriaReq mdmsCriteriaReq) {
		Map<String, List<Object>> tenantIdMap = MDMSApplicationRunnerImpl.getTenantMap();
		List<Object> list = tenantIdMap.get(mdmsCriteriaReq.getMdmsCriteria().getTenantId());
		
		if(list == null) 
		throw new CustomException("Invalid_tenantId.MdmsCriteria.tenantId","Invalid Tenant Id");
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ModuleDetail> moduleDetails = mdmsCriteriaReq.getMdmsCriteria().getModuleDetails();
		// String masterDataJson = null;
		String tenantJsonList = null;
		Map<String, Map<String, JSONArray>> moduleMap = new HashMap<>(); 
		
		try {
			// masterDataJson = objectMapper.writeValueAsString(tenantIdMap);
			tenantJsonList = objectMapper.writeValueAsString(list);
			log.info("MDMSService tenantJsonList:" + tenantJsonList);
			
			
			for(ModuleDetail moduleDetail : moduleDetails) {
				//List<Map<String, JSONArray>> response = new ArrayList<>();
				String moduleFilterJsonPath = "$[?(@.moduleName==".concat("\"").
						concat(moduleDetail.getModuleName()).concat("\"").concat(")]");
				log.info("moduleFilterJsonPath:"+ moduleFilterJsonPath);
				JSONArray mastersOfModule = JsonPath.read(tenantJsonList, moduleFilterJsonPath);
				log.info("getMaster moduleDetail:"+moduleDetail);
				Map<String, JSONArray> resMap = new HashMap<String, JSONArray>();
				for(MasterDetail masterDetail : moduleDetail.getMasterDetails()) {
					//String moduleJson = objectMapper.writeValueAsString(list);
					JSONArray masters = JsonPath.read(mastersOfModule, "$.*".concat(masterDetail.getName()).concat(".*"));
					log.info("masters:"+masters);
					if(masterDetail.getFilter() != null) 
						masters = filterMaster(masters, masterDetail.getFilter());
					
					resMap.put(masterDetail.getName(), masters);
					//response.add(resMap);
				}
				
				moduleMap.put(moduleDetail.getModuleName(), resMap);
				System.out.println("moduleMap:"+moduleMap);
				//response.clear();
			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		return moduleMap;
	}
	
	public JSONArray  filterMaster(JSONArray masters, String filterExp) {
		JSONArray filteredMasters = JsonPath.read(masters, filterExp);
		System.out.println("filteredMasters: "+filteredMasters);
		return filteredMasters;
	}

}
