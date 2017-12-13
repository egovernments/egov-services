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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Service
public class MDMSRequestValidator {

	public static final Logger logger = LoggerFactory.getLogger(MDMSRequestValidator.class);
	
	@Autowired
	private MDMSUtils mDMSUtils;

	@Autowired
	private MDMSService mDMSService;
	
	@Value("${cache.fetch.enabled}")
	private Boolean cacheFetch;
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> validateRequest(MDMSCreateRequest mDMSCreateRequest, List<String> keys,
			Boolean isCreate) throws Exception{
		ArrayList<Object> result = new ArrayList<>();
		Map<String, String> filePathMap = MDMSApplicationRunnerImpl.getFilePathMap();
		ObjectMapper mapper = new ObjectMapper();
		Object fileContents = null;
		Map<String, Object> masterContentFromCache = null;
		List<Object> masterData = new ArrayList<>();
		if(cacheFetch){
			masterContentFromCache = mDMSService.getContentFromCache(filePathMap, mDMSCreateRequest);
			if(null == masterContentFromCache){
				logger.info("Failed to get content from cache, fall back to fetch from git triggered....");
			}else{
			fileContents = mDMSService.getFileContents(filePathMap, mDMSCreateRequest);
			}
			
		}else{
			fileContents = mDMSService.getFileContents(filePathMap, mDMSCreateRequest);
		}
		if(null == fileContents){
			throw new CustomException("400","Invalid Tenant Id");
		}
		fileContents = mapper.writeValueAsString(fileContents);	
		if(null != masterContentFromCache){
			Object masterFromCache = mapper.writeValueAsString(masterContentFromCache);
			logger.info("masterContentsFromCache: "+masterContentFromCache);
			masterFromCache = JsonPath.read(masterFromCache.toString(), 
					"$.MdmsRes."+mDMSCreateRequest.getMasterMetaData().getModuleName()+"."+
					 mDMSCreateRequest.getMasterMetaData().getMasterName());
			logger.info("masterData fetched from cache: "+masterFromCache);
			
			DocumentContext documentContext = JsonPath.parse(fileContents.toString());
			documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(), masterFromCache);
			fileContents = documentContext.jsonString().toString();
		}
		Map<String, Object> allMasters = mapper.readValue(fileContents.toString(), Map.class);
		masterData = (List<Object>) allMasters.get(mDMSCreateRequest.getMasterMetaData().getMasterName());	
		Object requestMasterData = mDMSCreateRequest.getMasterMetaData().getMasterData();
		JSONArray masterDataArray = new JSONArray(mapper.writeValueAsString(requestMasterData));
		List<String> uniqueKeys = mDMSUtils.getUniqueKeys(mDMSCreateRequest, allMasters);
		if(null == uniqueKeys){
			throw new CustomException("400", "There are duplicate mdms-configs for this master: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
		}else if(uniqueKeys.isEmpty()){
			logger.info("Skipping Validation....");
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
