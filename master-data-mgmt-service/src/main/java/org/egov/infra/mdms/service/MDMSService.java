package org.egov.infra.mdms.service;

import java.util.List;
import java.util.Map;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.infra.mdms.model.MdmsCriteriaReq;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MDMSService {
	
	public String getMaster(MdmsCriteriaReq mdmsCriteriaReq) {
		Map<String, List<Object>> tenantIdMap = MDMSApplicationRunnerImpl.getTenantMap();  
		List<Object> list = tenantIdMap.get(mdmsCriteriaReq.getMdmsCriteria().getTenantId());
		ObjectMapper objectMapper = new ObjectMapper();
		//String masterDataJson = null;
		String tenantJsonList = null;
		try {
			 // masterDataJson = objectMapper.writeValueAsString(tenantIdMap);
			  tenantJsonList = objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		log.info("MDMSService tenantJsonList:"+tenantJsonList);
		List<Object> list1 = JsonPath.read(tenantJsonList,"$[?(@.moduleName==\"PT\")]");
		System.out.println(list1);
		//log.info("MDMSService masterDataJson:"+masterDataJson);
		
		//DocumentContext documentContext = JsonPath.parse(masterDataJson);
		//log.info("MDMSService documentContext:"+documentContext.json());
		//JsonPath.read("", "");
		return null;
	}

}
