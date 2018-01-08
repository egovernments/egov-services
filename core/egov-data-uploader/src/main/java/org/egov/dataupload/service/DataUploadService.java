package org.egov.dataupload.service;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.egov.DataUploadApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
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

import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	@Value("${result.file.path}")
	private String resultFilePath;
	
	@Value("${write.file.path}")
	private String writeFilePath;
	
	@Value("${response.file.name.prefix}")
	private String resFilePrefix;
	
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
			filePath = dataUploadRepository.getFileContents(uri.toString());
		}catch(Exception e){
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


	public SuccessFailure parseExcel(UploaderRequest uploaderRequest) throws Exception {
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
	    Definition uploadDefinition = dataUploadUtils.getUploadDefinition(uploadDefinitionMap, 
	    		uploadJob.getModuleName(), uploadJob.getDefName());
	    logger.info("HeaderMap: "+uploadDefinition.getHeaderJsonPathMap());
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
        	if(null != uploadDefinition.getIsParentChild() && uploadDefinition.getIsParentChild()){
        		uploadParentChildData(excelData, coloumnHeaders, uploadDefinition, uploaderRequest);
				dataUploadUtils.clearExceFile(writeFilePath);
				dataUploadUtils.clearExceFile(resultFilePath);
        	}else{
        		uploadData(excelData, coloumnHeaders, uploadDefinition, uploaderRequest);
				dataUploadUtils.clearExceFile(writeFilePath);
				dataUploadUtils.clearExceFile(resultFilePath);
        	}

        }catch(Exception e){
        	logger.info("Exception whil proccessing data: ",e);
        }
        
        SuccessFailure uploaderResponse = new SuccessFailure();
        uploaderResponse.setSuccess(success);
        uploaderResponse.setFailure(failure);
                
        return uploaderResponse;
	}
	
	private String uploadData(List<List<Object>> excelData,
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
		dataUploadUtils.writeToexcelSheet(coloumnHeaders);
		int successCount = 0; int failureCount = 0;
		for(List<Object> row: excelData){
			logger.info("row: "+row.toString());
			try{
				if(!row.isEmpty()){
					for(int i = 0; i < (coloumnHeaders.size() - additionFieldsCount); i++){
						logger.info("row val: "+row.get(i)+" coloumnHeader: "+coloumnHeaders.get(i));
		            	StringBuilder expression = new StringBuilder();
		            	String jsonPath = uploadDefinition.getHeaderJsonPathMap().get(coloumnHeaders.get(i).toString());
		            	if(null == jsonPath){
							logger.info("no jsonpath in config for: "+coloumnHeaders.get(i));
		            		continue;
		            	}
		            	String key = dataUploadUtils.getJsonPathKey(jsonPath, expression);
		            	if(key.contains("tenantId")){
			            	documentContext.put(expression.toString(), key, uploaderRequest.getUploadJobs().get(0).getTenantId());	            	
		            	}else{
		            		documentContext.put(expression.toString(), key, row.get(i));
		            	}
					} 	
				    try{
				    	documentContext.put("$", "RequestInfo", uploaderRequest.getRequestInfo());
					    request = documentContext.jsonString().toString();
				    }catch(Exception e){
				    	documentContext.put("$", "requestInfo", uploaderRequest.getRequestInfo());
					    request = documentContext.jsonString().toString();
				    }         	
				    Object apiResponse = hitApi(request, dataUploadUtils.getURI(uploadDefinition.getUri()));	
				    if(null == apiResponse){
				    	throw new CustomException("500", "Module API failed");
				    }
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
				    }
		    		row.add("SUCCESS");
		    		successCount++;
			    	logger.info("Writing SUCCESS ROW to excel....: "+row);
					dataUploadUtils.writeToexcelSheet(row);
				}
			}catch(Exception e){
				logger.error("Error while processing row: "+(excelData.indexOf(row) + 2), e);
		    	if(null != resJsonPathList){
		    		for(Object obj: resJsonPathList){
		    			row.add(null);
		    		}
		    		row.add("FAILED");
		    		if(!e.getMessage().isEmpty())
		    			row.add(e.getMessage());
		    		else
		    			row.add("Please verify config");			    	
		    		failureCount++;	
		    	}else{
		    		row.add("FAILED");
		    		if(!e.getMessage().isEmpty())
		    			row.add(e.getMessage());
		    		else
		    			row.add("Please verify config");
			    	failureCount++;	
		    	}
		    	logger.info("Writing FAILED ROW to excel....: "+row);
				dataUploadUtils.writeToexcelSheet(row);

		    	continue;
		    }			
		}
		String responseFilePath = getFileStoreId(uploadJob.getTenantId(), uploadJob.getModuleName(), uploadJob.getRequestFileName());
		uploadJob.setSuccessfulRows(successCount);uploadJob.setFailedRows(failureCount); uploadJob.setEndTime(new Date().getTime());
		uploadJob.setResponseFilePath(responseFilePath);uploadJob.setStatus(StatusEnum.fromValue("completed"));
		uploadRegistryRepository.updateJob(uploaderRequest);

		
	    return request;
	}
	
	
	private void uploadParentChildData(List<List<Object>> excelData,
		     List<Object> coloumnHeaders, Definition uploadDefinition, UploaderRequest uploaderRequest){
		ObjectMapper mapper = new ObjectMapper();
		if(null == uploadDefinition.getUniqueParentKeys()){
			//failed
		}
		
		String request = null;
	    int additionFieldsCount = 0;
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		uploadJob.setEndTime(0L);uploadJob.setFailedRows(0);uploadJob.setStartTime(new Date().getTime());uploadJob.setSuccessfulRows(0);
		uploadJob.setStatus(StatusEnum.fromValue("InProgress"));uploadJob.setResponseFilePath(null);uploadJob.setTotalRows(excelData.size() - 1);
		uploadRegistryRepository.updateJob(uploaderRequest);
		DocumentContext documentContext = null;
		DocumentContext bulkApiRequest = null;
		if(uploadDefinition.getIsBulkApi()){
			String value = JsonPath.read(uploadDefinition.getApiRequest(), 
	    			uploadDefinition.getArrayPath()).toString();
	    	documentContext = JsonPath.parse(value.substring(1, value.length() - 1));
	    	bulkApiRequest = JsonPath.parse(uploadDefinition.getApiRequest());
		}else{
			documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
		}
    	List<Object> resJsonPathList = null;
    	if(null != uploadDefinition.getAdditionalResFields()){
    		resJsonPathList = dataUploadUtils.getResJsonPathList(uploadDefinition.getAdditionalResFields(), coloumnHeaders);
    		additionFieldsCount = uploadDefinition.getAdditionalResFields().size();
    	}
    	coloumnHeaders.add("status"); coloumnHeaders.add("message");
    	additionFieldsCount+=2;
    	try{
    		dataUploadUtils.writeToexcelSheet(coloumnHeaders);
    	}catch(Exception e){
    		logger.error("Couldn't create job in db: ",e);
    	}
		int successCount = 0; int failureCount = 0;

		List<Integer> indexes = new ArrayList<>();
		
		//Getting indexes of parentKeys from header list to filter data based on those keys.
		for(String key: uploadDefinition.getUniqueParentKeys()){
			indexes.add(coloumnHeaders.indexOf(key));
		}
		
		for(int i = 0; i < excelData.size(); i++){
			List<List<Object>> filteredList = dataUploadUtils.filter(excelData, indexes, excelData.get(i));
			logger.info("filteredList: "+filteredList);
			
			/* Building a map that contains all row values of a given column. 
			This map will be used to construct any arrays present in the request.*/
			
			Map<String, List<Object>> allRowsOfAColumnMap = new HashMap<>();
			for(int j = 0; j <  (coloumnHeaders.size() - additionFieldsCount); j++){
				List<Object> rowsList = new ArrayList<>();
				for(List<Object> list: filteredList){
					rowsList.add(list.get(j));
				}
				allRowsOfAColumnMap.put(coloumnHeaders.get(j).toString(), rowsList);
			}
			logger.info("allRowsOfAColumnMap: "+allRowsOfAColumnMap);
			List<Map<String, Object>> filteredListObjects = new ArrayList<>();
			List<String> arrayKeys = new ArrayList<>();
			for(int k = 0; k < filteredList.size(); k++){
				for(int j = 0; j < (coloumnHeaders.size() - additionFieldsCount); j++){
	            	StringBuilder expression = new StringBuilder();
	            	String jsonPath = uploadDefinition.getHeaderJsonPathMap().get(coloumnHeaders.get(j).toString());
	            	if(null == jsonPath){
						logger.info("no jsonpath in config for: "+coloumnHeaders.get(j));
	            		continue;
	            	}
	            	jsonPath = jsonPath.replace(uploadDefinition.getArrayPath(), "$");
	            	if(jsonPath.contains("*")){
	            		String[] splitJsonPath = jsonPath.split("[.]");
	            		List<String> list = Arrays.asList(splitJsonPath);
	            		if(!(arrayKeys.contains(list.get((list.indexOf("*") - 1)))))
	            			arrayKeys.add(list.get((list.indexOf("*") - 1)));
	            	}
	            	String key = dataUploadUtils.getJsonPathKey(jsonPath, expression);
	            	if(key.contains("tenantId")){
		            	documentContext.put(expression.toString(), key, uploaderRequest.getUploadJobs().get(0).getTenantId());	            	
	            	}else{
	            		documentContext.put(expression.toString(), key, filteredList.get(k).get(j));
	            	}	            	
				}
				Type type = new TypeToken<Map<String, Object>>() {}.getType();
				Gson gson = new Gson();
				Map<String, Object> objectMap = gson.fromJson(documentContext.jsonString(), type);
            	filteredListObjects.add(objectMap);
			}
			Map<String, Object> requestMap = new HashMap<>();
			requestMap = filteredListObjects.get(0);
			for(Map<String, Object> map: filteredListObjects){
				if(0 != filteredListObjects.indexOf(map)) {
					for(String key: arrayKeys){
						List<Object> entry = (List<Object>) requestMap.get(key);
						entry.addAll((List<Object>) map.get(key));
						requestMap.put(key, entry);
					}
				}
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
			
			logger.info("FINAL REQUEST to EXTERNAL MODULE: "+request);
			i+=(filteredList.size() - 1);
		}
	}
		
		
	private Object hitApi(String request, String uri){
		logger.info("Making inter-service call to the module API...");
		Object response = null;
		logger.info("Request: "+request);
    	logger.info("uri: "+uri);
    	
	    Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Gson gson = new Gson();
		Map<String, Object> data = gson.fromJson(request, type);
		
    	response = dataUploadRepository.doApiCall(data, uri);
    	
    	logger.info("Module API Response: "+response);
    	
    	return response;
		
	}
	
	private String getFileStoreId(String tenantId, String module, String requestFileName) throws Exception{
		logger.info("Uploading result excel to filestore....");
		ObjectMapper mapper = new ObjectMapper();
		File from = new File(resultFilePath);
		File to = new File(resultFilePath.replace("result.xls", resFilePrefix+requestFileName+".xls"));
		boolean isRenameSuccess = from.renameTo(to);
		String filePath = null;
		if(isRenameSuccess){
			filePath = resultFilePath.replace("result.xls", resFilePrefix+requestFileName+".xls");
		}else{
			filePath = resultFilePath;
		}
		logger.info("result.xls renamed to: "+filePath);
		Map<String, Object> result = dataUploadRepository.postFileContents(tenantId, module, filePath);
		List<Object> objects = (List<Object>) result.get("files");
		String id = null;
		try{
			id = JsonPath.read(mapper.writeValueAsString(objects.get(0)), "$.fileStoreId");
		}catch(Exception e){
			logger.error("Couldn't fetch fileStore id from post file response: ",e);
		}
		logger.info("responsefile fileStoreId: "+id);
		try{
			File fromFile = new File(resultFilePath.replace("result.xls", resFilePrefix+requestFileName+".xls"));
			File toFile = new File(resultFilePath);
			boolean isSuccess = fromFile.renameTo(toFile);
			if(isSuccess)
				logger.info("result file renamed back to: "+resultFilePath);
		}catch(Exception e){
			logger.error("Renaming the file back to result.xls failed: ", e);
		}
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
				Defs def = Defs.builder().name(definition.getDefName()).build();
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
	
	
	
	
	
	private String prepareDataForBulkApi(List<List<Object>> excelData,
        List<String> jsonPathList, Definition uploadDefinition, RequestInfo requestInfo, List<ResponseMetaData> success,
	       List<ResponseMetaData> failure) throws JsonProcessingException{
					ObjectMapper mapper = new ObjectMapper();
					DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
					List<String> dataList = new ArrayList<>();
					try{
						for(List<Object> list: excelData){
							if(!list.isEmpty()){
					   		try{
							    	StringBuilder object = new StringBuilder(); 
							    	object.append(JsonPath.read(uploadDefinition.getApiRequest(), uploadDefinition.getArrayPath()).toString());
							    	object.deleteCharAt(0).deleteCharAt(object.toString().length() - 1);
							    	String json = mapper.writeValueAsString(JsonPath.read(uploadDefinition.getApiRequest(), uploadDefinition.getArrayPath()));
							    	json = json.substring(1, json.length() - 1);
							    	logger.info("json: "+json);
							    	DocumentContext docContext = JsonPath.parse(json);
							    	for(int i = 0; i < jsonPathList.size(); i++){
						            	StringBuilder expression = new StringBuilder();
						            	String key = dataUploadUtils.getJsonPathKey(jsonPathList.get(i), expression);
						            	documentContext.put("$", key, list.get(i));	            	
							    	}	    	
							    	dataList.add(docContext.jsonString().toString());
					   		}catch(Exception e){
					   			logger.error("error while parsing row (here row 1 is the first data row after headers): "+(excelData.indexOf(list) + 2), e);
					   			ResponseMetaData responseMetaData= new ResponseMetaData();
					   			responseMetaData.setRownum(excelData.indexOf(list) + 2);
					   			responseMetaData.setRowData(list);
					   			failure.add(responseMetaData);
					   			continue;
					   		}
					   	}	
						}
						StringBuilder expression = new StringBuilder();
						String key = dataUploadUtils.getJsonPathKey(uploadDefinition.getArrayPath(), expression);
					   Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
						Gson gson = new Gson();
						List<Map<String, Object>> array = gson.fromJson(dataList.toString(), type);
						documentContext.put(expression.toString(), key, array);	 
						try{
					   	documentContext.put("$", "RequestInfo", requestInfo);
						}catch(Exception e){
					   	documentContext.put("$", "requestInfo", requestInfo);
						}    	
						}catch(Exception e){
						logger.error("Exception caused while processing the excel data", e);
						throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), 
								"Exception caused while processing the excel data");
					}
					
					return documentContext.jsonString().toString();
		
		}
}