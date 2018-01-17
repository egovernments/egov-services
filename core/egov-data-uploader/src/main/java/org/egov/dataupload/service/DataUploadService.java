package org.egov.dataupload.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.assertj.core.util.Preconditions;
import org.egov.DataUploadApplicationRunnerImpl;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.Defs;
import org.egov.dataupload.model.JobSearchRequest;
import org.egov.dataupload.model.ModuleDefs;
import org.egov.dataupload.model.ResponseMetaData;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.dataupload.model.UploadJob;
import org.egov.dataupload.model.UploadJob.StatusEnum;
import org.egov.dataupload.model.UploaderRequest;
import org.egov.dataupload.model.SuccessFailure;
import org.egov.dataupload.producer.DataUploadProducer;
import org.egov.dataupload.repository.DataUploadRepository;
import org.egov.dataupload.repository.UploadRegistryRepository;
import org.egov.dataupload.utils.DataUploadUtils;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Service
public class DataUploadService {

	@Autowired
	private DataUploadRepository dataUploadRepository;
	
	@Autowired
	private UploadRegistryRepository uploadRegistryRepository;
	
	@Autowired
	private DataUploadApplicationRunnerImpl runner;
	
	@Autowired
	private DataUploadUtils dataUploadUtils;
	
	@Value("${filestore.host}")
	private String fileStoreHost;
	
	@Value("${filestore.get.endpoint}")
	private String getFileEndpoint;
		
	@Value("${response.file.name.prefix}")
	private String resFilePrefix;
	
	@Value("${template.download.prefix}")
	private String templateFilePrefix;
	
	@Autowired
	private DataUploadProducer dataUploadProducer;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadService.class);
	
	public List<UploadJob> createUploadJob(UploaderRequest uploaderRequest){
		StringBuilder uri = new StringBuilder();
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		if(null == uploadJob.getRequestFileName()){
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"Please provide the requestFileName.");
		}
		uri.append(fileStoreHost).append(getFileEndpoint);
		uri.append("?fileStoreId="+uploadJob.getRequestFilePath())
		   .append("&tenantId="+uploadJob.getTenantId());
		String filePath = null;
		try{
			filePath = dataUploadRepository.getFileContents(uri.toString(), uploaderRequest.getUploadJobs().get(0).getRequestFileName());
		}catch(Exception e){
			logger.info("FIle write exception: ",e);
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), 
					"No .xls file found for: fileStoreId = "+uploadJob.getRequestFilePath()
					+" AND tenantId = "+uploadJob.getTenantId());		
		}
		uploadJob.setCode(dataUploadUtils.mockIdGen(uploadJob.getModuleName(), uploadJob.getDefName()));
		uploadJob.setRequesterName(uploaderRequest.getRequestInfo().getUserInfo().getUserName());
		uploadRegistryRepository.createJob(uploaderRequest);
		uploadJob.setLocalFilePath(filePath);
		
		dataUploadProducer.producer(uploaderRequest);
		return uploaderRequest.getUploadJobs();
	}


	public void parseExcel(UploaderRequest uploaderRequest) throws Exception {
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
	    List<ResponseMetaData> success = new ArrayList<>();
	    List<ResponseMetaData> failure = new ArrayList<>();
	    List<List<Object>> excelData = new ArrayList<>();
        List<Object> coloumnHeaders = new ArrayList<>();
	    try{
		    MultipartFile file = dataUploadUtils.getExcelFile(uploadJob.getLocalFilePath());
			HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
	        HSSFSheet sheet = wb.getSheetAt(0);
	        excelData = dataUploadUtils.readExcelFile(sheet, coloumnHeaders);
	    }catch(Exception e){
	    	logger.error("Couldn't parse excel sheet", e);
	    }
        try{
    	    Definition uploadDefinition = dataUploadUtils.getUploadDefinition(uploadDefinitionMap, 
    	    		uploadJob.getModuleName(), uploadJob.getDefName());
    	    logger.info("HeaderMap: "+uploadDefinition.getHeaderJsonPathMap());
        	if(null != uploadDefinition.getIsParentChild() && uploadDefinition.getIsParentChild()){
        		uploadParentChildData(excelData, coloumnHeaders, uploadDefinition, uploaderRequest);
				dataUploadUtils.clearInternalDirectory();
        	}else{
        		uploadFlatData(excelData, coloumnHeaders, uploadDefinition, uploaderRequest);
				dataUploadUtils.clearInternalDirectory();
        	}

        }catch(Exception e){
        	logger.info("Exception while proccessing data: ",e);
    		uploadJob.setEndTime(new Date().getTime());uploadJob.setFailedRows(excelData.size());uploadJob.setSuccessfulRows(0);
    		uploadJob.setStatus(StatusEnum.fromValue("failed"));uploadJob.setTotalRows(excelData.size());
    		uploadRegistryRepository.updateJob(uploaderRequest);
    		
			dataUploadUtils.clearInternalDirectory();
        }
        
        SuccessFailure uploaderResponse = new SuccessFailure();
        uploaderResponse.setSuccess(success);
        uploaderResponse.setFailure(failure);                
	}
	
	private void uploadFlatData(List<List<Object>> excelData,
			     List<Object> coloumnHeaders, Definition uploadDefinition, UploaderRequest uploaderRequest) throws Exception{
		String request = null;
	    int additionFieldsCount = 0;
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		uploadJob.setEndTime(0L);uploadJob.setFailedRows(0);uploadJob.setStartTime(new Date().getTime());uploadJob.setSuccessfulRows(0);
		uploadJob.setStatus(StatusEnum.fromValue("InProgress"));uploadJob.setResponseFilePath(null);uploadJob.setTotalRows(excelData.size());
		uploadRegistryRepository.updateJob(uploaderRequest);
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
    	List<Object> resJsonPathList = null;
    	if(null != uploadDefinition.getAdditionalResFields()){
    		resJsonPathList = dataUploadUtils.getResJsonPathList(uploadDefinition.getAdditionalResFields(), coloumnHeaders);
    		additionFieldsCount = uploadDefinition.getAdditionalResFields().size();
    	}
    	coloumnHeaders.add("status"); coloumnHeaders.add("message");
    	additionFieldsCount+=2;
    	logger.info("Writing column headers to excel file....: "+coloumnHeaders);
    	String resultFilePath = dataUploadUtils.createANewFile(resFilePrefix + uploaderRequest.getUploadJobs().get(0).getRequestFileName());
		dataUploadUtils.writeToexcelSheet(coloumnHeaders, resultFilePath);
		Integer successCount = 0; Integer failureCount = 0;
		for(List<Object> row: excelData){
			String failureMessage = null;
			logger.info("row: "+row.toString());
				if(!row.isEmpty()){
					request = buildRequest(coloumnHeaders, additionFieldsCount, uploadDefinition, documentContext, uploaderRequest, row);
				    Object apiResponse = hitApi(request, dataUploadUtils.getURI(uploadDefinition.getUri()));	
				    if(null == apiResponse){
				    	failureMessage = "Module API failed with empty body in response";
				    }else {
				    	if(apiResponse instanceof String) {
				    		failureMessage = apiResponse.toString(); 
				    	}
				    	
				    }
				    List<Integer> successFailureCounts = writeToResultExcel(failureMessage, row, apiResponse, failureCount, successCount, resultFilePath, resJsonPathList);
				    failureCount = successFailureCounts.get(0);
				    successCount = successFailureCounts.get(1);
			}
			String responseFilePath = getFileStoreId(uploadJob.getTenantId(), uploadJob.getModuleName(), resultFilePath);
			uploadJob.setSuccessfulRows(successCount);uploadJob.setFailedRows(failureCount); uploadJob.setEndTime(new Date().getTime());
			uploadJob.setResponseFilePath(responseFilePath);uploadJob.setStatus(StatusEnum.fromValue("completed"));
			uploadRegistryRepository.updateJob(uploaderRequest);
		
		}
	}
	
	public String buildRequest(List<Object> coloumnHeaders, int additionFieldsCount, 
			Definition uploadDefinition, DocumentContext documentContext, UploaderRequest uploaderRequest, List<Object>row) {
		String request = null;
		logger.debug("row size: "+row.size());
		logger.debug("coloumnHeaders size: "+coloumnHeaders.size());
		logger.debug("additionFieldsCount: "+additionFieldsCount);
		for(int i = 0; i < (coloumnHeaders.size() - additionFieldsCount); i++){
			logger.debug("row val: "+row.get(i)+" coloumnHeader: "+coloumnHeaders.get(i));			
        	List<String> jsonPathList = uploadDefinition.getHeaderJsonPathMap().get(coloumnHeaders.get(i).toString());
        	if(null == jsonPathList){
				logger.info("no jsonpath in config for: "+coloumnHeaders.get(i));
        		continue;
        	}
        	if(jsonPathList.isEmpty()) {
				logger.info("no jsonpath in config for: "+coloumnHeaders.get(i));
        		continue;
        	}
        	for(String jsonPath: jsonPathList) {
            	StringBuilder expression = new StringBuilder();
            	String key = dataUploadUtils.getJsonPathKey(jsonPath, expression);
            	documentContext.put(expression.toString(), key, row.get(i));
        	}
		} 	
		logger.info("Adding tenantId...");
		for(String path: uploadDefinition.getTenantIdPaths()) {
        	StringBuilder expression = new StringBuilder();
        	String key = dataUploadUtils.getJsonPathKey(path, expression);
        	documentContext.put(expression.toString(), key, uploaderRequest.getUploadJobs().get(0).getTenantId());
		}
	    try{
	    	documentContext.put("$", "RequestInfo", uploaderRequest.getRequestInfo());
		    request = documentContext.jsonString().toString();
	    }catch(Exception e){
	    	documentContext.put("$", "requestInfo", uploaderRequest.getRequestInfo());
		    request = documentContext.jsonString().toString();
	    } 
	    
	    
	    return request;
	}
	
	public List<Integer> writeToResultExcel(String failureMessage, List<Object> row, Object apiResponse, Integer failureCount,
			Integer successCount, String resultFilePath, List<Object> resJsonPathList) throws Exception {
		List<Integer> successFailureCounts = new ArrayList<>();
	    if(null != failureMessage && !failureMessage.isEmpty()) {
	    	if(null != resJsonPathList){
	    		for(Object obj: resJsonPathList){
	    			row.add(null);
	    		}
	    		row.add("FAILED");
	    		row.add(failureMessage);			    	
	    		failureCount++;	
	    	}else{
	    		row.add("FAILED");
	    		row.add(failureMessage);			    	
		    	failureCount++;	
	    	}
	    	logger.info("Writing FAILED ROW to excel....: "+row);
			dataUploadUtils.writeToexcelSheet(row, resultFilePath);
	    	
	    }else {
		    if(null != resJsonPathList){
		    	try{
		    		dataUploadUtils.addAdditionalFields(apiResponse, row, resJsonPathList);
		    		row.add("SUCCESS");
		    	}catch(Exception e){
		    		for(Object obj: resJsonPathList){
		    			row.add(null);
		    		}
		    		row.add("SUCCESS");
		    		row.add("Failed to obtain additional fields from API response");
		    		successCount++;
		    	}
		    }else {
	    		row.add("SUCCESS");
	    		successCount++;
		    }
	    	logger.info("Writing SUCCESS ROW to excel....: "+row);
			dataUploadUtils.writeToexcelSheet(row, resultFilePath);
	    }
		successFailureCounts.add(failureCount);
		successFailureCounts.add(successCount);
		
	    return successFailureCounts;
	}
	
	private void uploadParentChildData(List<List<Object>> excelData,
		     List<Object> coloumnHeaders, Definition uploadDefinition, UploaderRequest uploaderRequest) throws Exception{
		ObjectMapper mapper = new ObjectMapper();		
		String request = null;
	    int additionFieldsCount = 0;
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		uploadJob.setEndTime(0L);uploadJob.setFailedRows(0);uploadJob.setStartTime(new Date().getTime());uploadJob.setSuccessfulRows(0);
		uploadJob.setStatus(StatusEnum.fromValue("InProgress"));uploadJob.setResponseFilePath(null);uploadJob.setTotalRows(excelData.size() - 1);
		uploadRegistryRepository.updateJob(uploaderRequest);
		if(null == uploadDefinition.getUniqueParentKeys()){
			logger.error("No parent unique keys.");
    		uploadJob.setEndTime(new Date().getTime());uploadJob.setFailedRows(excelData.size());uploadJob.setSuccessfulRows(0);
    		uploadJob.setStatus(StatusEnum.fromValue("failed"));uploadJob.setTotalRows(excelData.size());
    		uploadRegistryRepository.updateJob(uploaderRequest);
			dataUploadUtils.clearInternalDirectory();
		}		
		DocumentContext documentContext = null;DocumentContext bulkApiRequest = null;
		if(uploadDefinition.getIsBulkApi()){
			String value = JsonPath.read(uploadDefinition.getApiRequest(), 
	    			uploadDefinition.getArrayPath()).toString();
			//Module specific content of the request body
	    	documentContext = JsonPath.parse(value.substring(1, value.length() - 1));
	    	//Actual request with RequestInfo and module specific content
	    	bulkApiRequest = JsonPath.parse(uploadDefinition.getApiRequest());
		}else{
	    	//Actual request with RequestInfo and module specific content
			documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
		}
    	List<Object> resJsonPathList = null;
    	if(null != uploadDefinition.getAdditionalResFields()){
    		resJsonPathList = dataUploadUtils.getResJsonPathList(uploadDefinition.getAdditionalResFields(), coloumnHeaders);
    		additionFieldsCount = uploadDefinition.getAdditionalResFields().size();
    	}
    	coloumnHeaders.add("status"); coloumnHeaders.add("message");
    	additionFieldsCount+=2;
    	String resultFilePath = dataUploadUtils.createANewFile(resFilePrefix + uploaderRequest.getUploadJobs().get(0).getRequestFileName());
		dataUploadUtils.writeToexcelSheet(coloumnHeaders, resultFilePath);
		//Till now the coloumnheaders have been written to result xls. Content processing begins now.
		
		int successCount = 0; int failureCount = 0;
		List<Integer> indexes = new ArrayList<>();
		
		//Getting indexes of parentKeys from header list to filter data based on those keys.
		for(String key: uploadDefinition.getUniqueParentKeys()){
			indexes.add(coloumnHeaders.indexOf(key));
		}
		for(int i = 0; i < excelData.size(); i++){
			String failureMessage = null;
			//fetching list of all the rows that will be combined to form ONE request.
			List<List<Object>> filteredList = dataUploadUtils.filter(excelData, indexes, excelData.get(i));			
			request = buildRequestForParentChild(i, filteredList, coloumnHeaders, additionFieldsCount, uploadDefinition, 
					documentContext, uploaderRequest, bulkApiRequest);
			
			logger.info("FINAL REQUEST to EXTERNAL MODULE: "+request);
			
		    Object apiResponse = hitApi(request, dataUploadUtils.getURI(uploadDefinition.getUri()));	
		    if(null == apiResponse){
		    	failureMessage = "Module API failed with empty body in response";
		    }else {
		    	if(apiResponse instanceof String) {
		    		failureMessage = apiResponse.toString(); 
		    	}
		    	
		    }
		    
		    writeToExcelForParentChild(failureMessage, apiResponse, failureCount, successCount, 
		    		resultFilePath, resJsonPathList, filteredList);
		    
	    	//counter is incremented based on no of rows processed in this iteration.
			i+=(filteredList.size() - 1);
	    }
		String responseFilePath = getFileStoreId(uploadJob.getTenantId(), uploadJob.getModuleName(), resultFilePath);
		uploadJob.setSuccessfulRows(successCount);uploadJob.setFailedRows(failureCount); uploadJob.setEndTime(new Date().getTime());
		uploadJob.setResponseFilePath(responseFilePath);uploadJob.setStatus(StatusEnum.fromValue("completed"));
		uploadRegistryRepository.updateJob(uploaderRequest);
}
		
		
	private Object hitApi(String request, String uri){
		logger.info("Making inter-service call to the module API...");
		Object response = null;
		logger.info("Request: "+request);
    	logger.info("URI: "+uri);
    	
	    Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Gson gson = new Gson();
		Map<String, Object> data = gson.fromJson(request, type);
		try {
			response = dataUploadRepository.doApiCall(data, uri);
		}catch(Exception e) {
			logger.error("Failed to parse error object",e);
		}
    	    	
    	return response;
		
	}
	
	private String getFileStoreId(String tenantId, String module, String filePath) throws Exception{
		logger.info("Uploading result excel to filestore....");
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = dataUploadRepository.postFileContents(tenantId, module, filePath);
		List<Object> objects = (List<Object>) result.get("files");
		String id = null;
		try{
			id = JsonPath.read(mapper.writeValueAsString(objects.get(0)), "$.fileStoreId");
		}catch(Exception e){
			logger.error("Couldn't fetch fileStore id from post file response: ",e);
		}
		logger.info("responsefile fileStoreId: "+id);
		
		return id;
	}
	
	
	public List<ModuleDefs> getModuleDefs(){
		logger.info("fetching definitions for upload....");
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
		List<ModuleDefs> result = new ArrayList<>();
		for(Entry<String, UploadDefinition> entry: uploadDefinitionMap.entrySet()){
			ModuleDefs moduleDefs = new ModuleDefs();
			moduleDefs.setName(entry.getKey());
			List<Defs> definitions = new ArrayList<>();
			UploadDefinition uploadDefinition = entry.getValue();
			for(Definition definition: uploadDefinition.getDefinitions()){
				Defs def = Defs.builder().name(definition.getDefName()).
						   templatePath(templateFilePrefix + "/" + definition.getTemplateFileName()).build();
				definitions.add(def);
			}
			moduleDefs.setDefinitions(definitions);
			
			result.add(moduleDefs);
		}
		logger.info("result: "+result);
		return result;

	}
	
	
	public List<UploadJob> getUploadJobs(JobSearchRequest jobSearchRequest){
		logger.info("fetching upload jobs....");
		logger.info("JobSearchRequest: "+jobSearchRequest.toString());
		List<UploadJob> uploadJobs = new ArrayList<>();
		try{
			uploadJobs = uploadRegistryRepository.searchJob(jobSearchRequest);
		}catch(Exception e){
			logger.error("Exception while searching for jobs", e);
		}

		return uploadJobs;

	}
	
	private String buildRequestForParentChild(int i, List<List<Object>> filteredList,
			List<Object> coloumnHeaders, int additionFieldsCount, Definition uploadDefinition, DocumentContext documentContext, 
			UploaderRequest uploaderRequest, DocumentContext bulkApiRequest) throws Exception {
		String request = null;
		ObjectMapper mapper = new ObjectMapper();
		logger.info("filteredList: "+filteredList);
		List<Map<String, Object>> filteredListObjects = new ArrayList<>();
		for(int k = 0; k < filteredList.size(); k++){
			for(int j = 0; j < (coloumnHeaders.size() - additionFieldsCount); j++){
            	StringBuilder expression = new StringBuilder();
            	List<String> jsonPathList = uploadDefinition.getHeaderJsonPathMap().get(coloumnHeaders.get(j).toString());
            	if(null == jsonPathList){
					logger.info("no jsonpath in config for: "+coloumnHeaders.get(i));
            		continue;
            	}
            	if(jsonPathList.isEmpty()) {
					logger.info("no jsonpath in config for: "+coloumnHeaders.get(i));
            		continue;
            	}
            	for(String jsonPath: jsonPathList) {
            		if(uploadDefinition.getIsBulkApi()) {
            			jsonPath = jsonPath.replace(uploadDefinition.getArrayPath(), "$");
            			//Because for bulk API, multiple such objects might have to be created, all of them will be appended at once in the end.
            		}
	            	String key = dataUploadUtils.getJsonPathKey(jsonPath, expression);
	            	if(key.contains("tenantId")){
		            	documentContext.put(expression.toString(), key, uploaderRequest.getUploadJobs().get(0).getTenantId());	            	
	            	}else{
	            		documentContext.put(expression.toString(), key, filteredList.get(k).get(j));
	            	}
            	}	            	
			}
			Type type = new TypeToken<Map<String, Object>>() {}.getType();
			Gson gson = new Gson();
			Map<String, Object> objectMap = gson.fromJson(documentContext.jsonString(), type);
        	filteredListObjects.add(objectMap);
		}
		List<String> uniqueKeysForInnerObject = new ArrayList<>();
		for(String col: uploadDefinition.getUniqueKeysForInnerObject()) {
		    StringBuilder expression = new StringBuilder();
        	String key = dataUploadUtils.getJsonPathKey(uploadDefinition.getHeaderJsonPathMap().get(col).get(0), expression);
        	uniqueKeysForInnerObject.add(key);
		}
		Map<String, Object> requestMap = new HashMap<>();
		for(Map map: filteredListObjects) {
			deepMerge(requestMap, map, uniqueKeysForInnerObject);
		}
		logger.info("requestMap: "+requestMap);
		Object requestContentBody = null;
		try{
			requestContentBody = mapper.writeValueAsString(requestMap);
		}catch(Exception e){
			logger.error("Exception while parsing requestMap to String", e);
		}
		if(uploadDefinition.getIsBulkApi()){
		    try{
		    	bulkApiRequest.put("$", "RequestInfo", uploaderRequest.getRequestInfo());
		    }catch(Exception e){
		    	bulkApiRequest.put("$", "requestInfo", uploaderRequest.getRequestInfo());
		    }
		    StringBuilder expression = new StringBuilder();
        	String key = dataUploadUtils.getJsonPathKey(uploadDefinition.getArrayPath().substring(0, uploadDefinition.getArrayPath().length() - 1), expression);
        	bulkApiRequest.put(expression.toString(), key, requestMap);
        	request = bulkApiRequest.jsonString().toString();
		}else{
			DocumentContext docContext = JsonPath.parse(requestContentBody);
		    try{
		    	docContext.put("$", "RequestInfo", uploaderRequest.getRequestInfo());
			    request = docContext.jsonString().toString();
		    }catch(Exception e){
		    	docContext.put("$", "requestInfo", uploaderRequest.getRequestInfo());
			    request = docContext.jsonString().toString();
		    }  
		}
		
		return request;
	}
	
	@SuppressWarnings("rawtypes")
	private static Map deepMerge(Map original, Map newMap, List<String> uniqueKeysForInnerObject) {
		if(uniqueKeysForInnerObject.isEmpty()) {
			logger.error("uniqueKeysForInnerObject is Empty!");
		}
		logger.info("uniqueKeysForInnerObject: "+uniqueKeysForInnerObject);
	    for (Object key : newMap.keySet()) {
			logger.info("key: "+key);
	        if (newMap.get(key) instanceof Map && original.get(key) instanceof Map) {
	            Map originalChild = (Map) original.get(key);
	            Map newChild = (Map) newMap.get(key);
	            original.put(key, deepMerge(originalChild, newChild, uniqueKeysForInnerObject));
	        } else if (newMap.get(key) instanceof List && original.get(key) instanceof List) {
	    		logger.info("Instance of list: ");
	            List originalChild = (List) original.get(key);
	    		logger.info("originalChild: "+originalChild);
	            List newChild = (List) newMap.get(key);
	    		logger.info("newChild: "+newChild);
	    		Map<Object, Object> originalChildEntry = (Map) originalChild.get(originalChild.size() - 1);
	    		Map<Object, Object> newChildEntry = (Map) newChild.get(0);
	    		int counter = 0;
	    		try {
		    		for(String mapKey: uniqueKeysForInnerObject) {
		    			if(originalChildEntry.get(mapKey).
		    					equals(newChildEntry.get(mapKey))) {
		    				counter++;
		    				continue;
		    			}else {
		    				break;
		    			}
		    		}
		    		if(counter == uniqueKeysForInnerObject.size() && counter != 0) {
		    			logger.info("Match found!");
		    			for(Object originalMapKey: originalChildEntry.keySet()) {
		    				if(originalChildEntry.get(originalMapKey) instanceof List) {
		    					List newChildValue = (List) newChildEntry.get(originalMapKey);
		    					List originalChildValue = (List) originalChildEntry.get(originalMapKey);
		    					originalChildValue.addAll(newChildValue);
		    					originalChildEntry.put(originalMapKey, originalChildValue);
		    				}
		    			}
		    		}else {
		    			logger.info("No match found!");
		    			originalChild.add(newChildEntry);
		    		}
	    		}catch(Exception e) {
	                if (!originalChild.contains(newChildEntry)) {
	                    originalChild.add(newChildEntry);
	                    continue;
	    		    }
	    	    }	
	        } else {
	    		logger.info("Adding new key: "+key+" value: "+newMap.get(key)+" to original");
	            original.put(key, newMap.get(key));
	        }	        
	    }
	    return original;
	}
	
	public void writeToExcelForParentChild(String failureMessage, Object apiResponse, int failureCount, int successCount, 
			String resultFilePath, List<Object> resJsonPathList, List<List<Object>> filteredList) throws Exception {
	    if(null != failureMessage && !failureMessage.isEmpty()) {
	    	for(List<Object> row: filteredList) {
		    	if(null != resJsonPathList){
		    		for(Object obj: resJsonPathList){
		    			row.add(null);
		    		}
		    		row.add("FAILED");
		    		row.add(failureMessage);			    	
		    		failureCount++;	
		    	}else{
		    		row.add("FAILED");
		    		row.add(failureMessage);			    	
			    	failureCount++;	
		    	}
		    	logger.info("Writing FAILED ROW to excel....: "+row);
				dataUploadUtils.writeToexcelSheet(row, resultFilePath);
	    	}
	    	
	    }else {
	    	for(List<Object> row: filteredList) {
			    if(null != resJsonPathList){
			    	try{
			    		dataUploadUtils.addAdditionalFields(apiResponse, row, resJsonPathList);
			    		row.add("SUCCESS");
			    	}catch(Exception e){
			    		for(Object obj: resJsonPathList){
			    			row.add(null);
			    		}
			    		row.add("SUCCESS");
			    		row.add("Failed to obtain additional fields from API response");
			    		successCount++;
			    	}
			    }else {
		    		row.add("SUCCESS");
		    		successCount++;
			    }
		    	logger.info("Writing SUCCESS ROW to excel....: "+row);
				dataUploadUtils.writeToexcelSheet(row, resultFilePath);
	    	}
	    }
	}
}