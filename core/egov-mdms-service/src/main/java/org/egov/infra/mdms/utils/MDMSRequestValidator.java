package org.egov.infra.mdms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
		List<Object> allmasterConfigs = new ArrayList<>();
		masterData = (List<Object>) allMasters.get(mDMSCreateRequest.getMasterMetaData().getMasterName());
		allmasterConfigs = (List<Object>) allMasters.get("mdms-config");
		List<Object> masterConfig = filter(allmasterConfigs, "$.masterName", mDMSCreateRequest.getMasterMetaData().getMasterName());		
		if(masterConfig.size() > 1){
			throw new CustomException("400", "There are duplicate mdms-configs for this master: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
		}else if(masterConfig.isEmpty()){
			throw new CustomException("400", "There is no mdms-config for this master: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
		}
		logger.info("Master Name: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
		logger.info("Master Data: "+masterData);
		logger.info("MDMS Config: "+masterConfig.get(0).toString());
		
		List<String> uniqueKeys = JsonPath.read(masterConfig.get(0).toString(), "$.uniqueKeys");
		logger.info("uniqueKeys: "+uniqueKeys);
		Object requestMasterData = mDMSCreateRequest.getMasterMetaData().getMasterData();
		JSONArray masterDataArray = new JSONArray(mapper.writeValueAsString(requestMasterData));
		for(int i = 0; i < masterDataArray.length() ; i++){
			List<Object> filterResult = new ArrayList<>();			
			for(String key: uniqueKeys){
				logger.info("key: "+key);
				Object value;
				value = JsonPath.read(masterDataArray.get(i).toString(), key.toString());
				filterResult = filter(masterData, key, value);
				masterData = filterResult;
				logger.info("filterResult: "+filterResult);				

			}
			logger.info("filterResult: "+filterResult);				
			if(!filterResult.isEmpty())
				result.add(masterDataArray.get(i));

		}
		logger.info("Validation result, List of error objects: "+result);
		return result;
	}
	
	public List<Object> filter(List<Object> list, String key, Object value) throws JsonProcessingException{
		List<Object> filteredList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try{
			filteredList = list.parallelStream()
					.filter(obj -> true == ((JsonPath.read(obj.toString(), key.toString())).equals(value)))
					.collect(Collectors.toList());
		}catch(Exception e){
			filteredList = list.parallelStream()
					.map(obj -> {
						try{
							return mapper.writeValueAsString(obj);
						}catch(Exception ex){
							logger.error("Parsing error inside the stream: ",ex);
						}
						return null;
					})
					.filter(obj -> true == ((JsonPath.read(obj, key.toString())).equals(value)))
					.collect(Collectors.toList());	
		}
		return filteredList;
	}
	
}
