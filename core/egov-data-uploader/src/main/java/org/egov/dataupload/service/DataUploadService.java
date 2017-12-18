package org.egov.dataupload.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.egov.DataUploadApplicationRunnerImpl;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.Defs;
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
	
	@Value("${result.file.path}")
	private String resultFilePath;
	
	@Value("${write.file.path}")
	private String writeFilePath;
	
	@Autowired
	private DataUploadProducer dataUploadProducer;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadService.class);
	
	public List<UploadJob> createUploadJob(UploaderRequest uploaderRequest){
		StringBuilder uri = new StringBuilder();
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
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
		uploadJob.setCode(dataUploadUtils.mockIdGen(uploadJob.getModuleName()));
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
		    /*if(uploadDefinition.getIsBulkApi()){
		    	String request = prepareDataForBulkApi(excelData, coloumnHeaders, 
		    			uploadDefinition, uploaderRequest.getRequestInfo(), success, failure);
		        hitApi(request, uploadDefinition.getUri());
				ResponseMetaData responseMetaData= new ResponseMetaData();
				responseMetaData.setRowData(excelData);
	        	success.add(responseMetaData);
		    }else*/{
		    	uploadData(excelData, coloumnHeaders, uploadDefinition, uploaderRequest);
				dataUploadUtils.clearExceFile(writeFilePath);
				dataUploadUtils.clearExceFile(resultFilePath);
		    }
        }catch(Exception e){
			ResponseMetaData responseMetaData= new ResponseMetaData();
			responseMetaData.setRowData(excelData);
        	failure.add(responseMetaData);
        }
        
        SuccessFailure uploaderResponse = new SuccessFailure();
        uploaderResponse.setSuccess(success);
        uploaderResponse.setFailure(failure);
        
        logger.info("UploaderResponse: "+uploaderResponse);
        
        return uploaderResponse;
	}
	
	private String uploadData(List<List<Object>> excelData,
			     List<Object> coloumnHeaders, Definition uploadDefinition, UploaderRequest uploaderRequest) throws Exception{
		String request = null;
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		uploadJob.setEndTime(0L);uploadJob.setFailedRows(0);uploadJob.setStartTime(new Date().getTime());uploadJob.setSuccessfulRows(0);
		uploadJob.setStatus(StatusEnum.fromValue("InProgress"));uploadJob.setResponseFilePath(null);uploadJob.setTotalRows(excelData.size());
		uploadRegistryRepository.updateJob(uploaderRequest);
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
    	List<Object> resJsonPathList = null;
    	if(null != uploadDefinition.getAdditionalResFields()){
    		resJsonPathList = dataUploadUtils.getResJsonPathList(uploadDefinition.getAdditionalResFields(), coloumnHeaders);
    	}
    	coloumnHeaders.add("status"); coloumnHeaders.add("message");
		dataUploadUtils.writeToexcelSheet(coloumnHeaders);
		int successCount = 0; int failureCount = 0;
		for(List<Object> row: excelData){
			try{
				if(!row.isEmpty()){
					for(int i = 0; i < coloumnHeaders.size(); i++){
		            	StringBuilder expression = new StringBuilder();
		            	String jsonPath = uploadDefinition.getHeaderJsonPathMap().get(coloumnHeaders.get(i));
		            	String key = dataUploadUtils.getJsonPathKey(jsonPath, expression);
				        documentContext.put(expression.toString(), key, row.get(i));	            	
					} 	
				    logger.info("RequestInfo: "+uploaderRequest.getRequestInfo());
				    try{
				    	documentContext.put("$", "RequestInfo", uploaderRequest.getRequestInfo());
					    request = documentContext.jsonString().toString();
				    }catch(Exception e){
				    	documentContext.put("$", "requestInfo", uploaderRequest.getRequestInfo());
					    request = documentContext.jsonString().toString();
				    }         	
				    Object apiResponse = hitApi(request, uploadDefinition.getUri());	
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
					dataUploadUtils.writeToexcelSheet(row);
				}
			}catch(Exception e){
				logger.error("Error while processing row: "+(excelData.indexOf(row) + 2));
		    	if(null != resJsonPathList){
		    		for(Object obj: resJsonPathList){
		    			row.add(null);
		    		}
		    		row.add("FAILED");
		    		row.add(e.getMessage());
			    	failureCount++;	
		    	}else{
		    		row.add("FAILED");
		    		row.add(e.getMessage());
			    	failureCount++;	
		    	}
				dataUploadUtils.writeToexcelSheet(row);

		    	continue;
		    }			
		}
		String responseFilePath = getFileStoreId(uploadJob.getTenantId(), uploadJob.getModuleName());
		uploadJob.setSuccessfulRows(successCount);uploadJob.setFailedRows(failureCount);
		uploadJob.setResponseFilePath(responseFilePath);uploadJob.setStatus(StatusEnum.fromValue("completed"));
		uploadRegistryRepository.updateJob(uploaderRequest);

		
	    return request;
	}
		
		
	private Object hitApi(String request, String uri){
		Object response = null;
		logger.info("Request: "+request);
    	logger.info("uri: "+uri);
    	
	    Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Gson gson = new Gson();
		Map<String, Object> data = gson.fromJson(request, type);
		
    	response = dataUploadRepository.doApiCall(data, uri);
    	
    	return response;
		
	}
	
	private String getFileStoreId(String tenantId, String module) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = dataUploadRepository.postFileContents(tenantId, module, resultFilePath);
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
				Defs def = Defs.builder().name(definition.getDefName()).build();
				definitions.add(def);
			}
			moduleDefs.setDefinitions(definitions);
			
			result.add(moduleDefs);
		}
		logger.info("result: "+result);
		return result;

	}
	
	
	
	
	
	/*private String prepareDataForBulkApi(List<List<Object>> excelData,
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

}*/
}