package org.egov.infra.mdms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.mdms.model.MDMSCreateRequest;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class MDMSRequestValidator {

	public static final Logger logger = LoggerFactory.getLogger(MDMSRequestValidator.class);

	@SuppressWarnings("unchecked")
	public ArrayList<Object> validateCreateRequest(MDMSCreateRequest mDMSCreateRequest) throws Exception{
		ArrayList<Object> result = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, Object>> validationMap = MDMSApplicationRunnerImpl.getValidationMap();
		Map<String, Object> allMasters = 
				validationMap.get(mDMSCreateRequest.getMasterMetaData().getTenantId() +"-"+ mDMSCreateRequest.getMasterMetaData().getModuleName());
		List<Object> masterData = new ArrayList<>();
		masterData = (List<Object>) allMasters.get(mDMSCreateRequest.getMasterMetaData().getMasterName());
		logger.info("Master Name: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
		logger.info("Master Data: "+masterData);
		
		Object requestMasterData = mDMSCreateRequest.getMasterMetaData().getMasterData();
		JSONArray masterDataArray = new JSONArray(mapper.writeValueAsString(requestMasterData));
		for(int i = 0; i < masterDataArray.length() ; i++){
			Object id = JsonPath.read(masterDataArray.get(i).toString(), "$.id");
			Object tenantId = JsonPath.read(masterDataArray.get(i).toString(), "$.tenantId");
			List<Object> filterResult = new ArrayList<>();			
			filterResult = masterData.parallelStream()
					.map(obj -> {
						try{
							return mapper.writeValueAsString(obj);
						}catch(Exception e){
							logger.error("Parsing error inside stream: ",e);
						}
						return null;
					})
					.filter(obj -> true == (((JsonPath.read(obj, "$.id")).equals(id)) && ((JsonPath.read(obj, "$.tenantId")).equals(tenantId))))
					.collect(Collectors.toList());
									
			if(!filterResult.isEmpty())
				result.add(masterDataArray.get(i));

		}
		logger.info("Validation result, List of error objects: "+result);
		return result;
	}
}
