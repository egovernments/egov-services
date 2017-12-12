package org.egov.infra.mdms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.infra.mdms.service.MDMSService;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class MDMSRequestValidator {

	public static final Logger logger = LoggerFactory.getLogger(MDMSRequestValidator.class);
	
	@Autowired
	private MDMSUtils mDMSUtils;

	@Autowired
	private MDMSService mDMSService;
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> validateRequest(MDMSCreateRequest mDMSCreateRequest, List<String> keys,
			Boolean isCreate) throws Exception{
		ArrayList<Object> result = new ArrayList<>();
		Map<String, String> filePathMap = MDMSApplicationRunnerImpl.getFilePathMap();
		ObjectMapper mapper = new ObjectMapper();
		Object fileContents = mDMSService.getFileContents(filePathMap, mDMSCreateRequest);
		if(null == fileContents) 
			throw new CustomException("400","Invalid Tenant Id");
		fileContents = mapper.writeValueAsString(fileContents);		
		List<Object> masterData = new ArrayList<>();
		Map<String, Object> allMasters = mapper.readValue(fileContents.toString(), Map.class);
		masterData = (List<Object>) allMasters.get(mDMSCreateRequest.getMasterMetaData().getMasterName());	
		Object requestMasterData = mDMSCreateRequest.getMasterMetaData().getMasterData();
		JSONArray masterDataArray = new JSONArray(mapper.writeValueAsString(requestMasterData));
		List<String> uniqueKeys = mDMSUtils.getUniqueKeys(mDMSCreateRequest, allMasters);
		if(null == uniqueKeys){
			throw new CustomException("400", "There are duplicate mdms-configs for this master: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
		}else if(uniqueKeys.isEmpty()){
			logger.info("Skipping Validation....");
			/*for(int i = 0; i < masterDataArray.length() ; i++){
				Object arrayElement = masterDataArray.get(i);
				List<Object> filterResult = mDMSUtils.filter(masterData, arrayElement);			
				if(!filterResult.isEmpty())
					result.add(masterDataArray.get(i));
			}*/
			return result;
		}else {
			logger.info("uniqueKeys: "+uniqueKeys);
			keys.addAll(uniqueKeys);
			List<Object> masters = masterData;
			for(int i = 0; i < masterDataArray.length() ; i++){
				List<Object> filterResult = new ArrayList<>();
				masterData = masters;
				for(String key: uniqueKeys){
					Object value;
					value = JsonPath.read(masterDataArray.get(i).toString(), key.toString());
					filterResult = mDMSUtils.filter(masterData, key, value);
					masterData = filterResult;
					logger.info("filterResult: "+filterResult);				
	
				}
				logger.info("filterResult: "+filterResult);	
				
				if(isCreate){
		     		if(!filterResult.isEmpty())
						result.add(masterDataArray.get(i));
				}else{
		     		if(filterResult.isEmpty())
						result.add(masterDataArray.get(i));
				}
	
			}
			logger.info("Validation result, List of error objects: "+result);
			return result;
		}
	}
	
}
