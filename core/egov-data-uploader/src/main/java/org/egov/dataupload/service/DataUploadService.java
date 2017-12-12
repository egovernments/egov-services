package org.egov.dataupload.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.egov.DataUploadApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.ResponseMetaData;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.dataupload.model.UploaderRequest;
import org.egov.dataupload.model.UploaderResponse;
import org.egov.dataupload.repository.DataUploadRepository;
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
	private DataUploadApplicationRunnerImpl runner;
	
	@Autowired
	private DataUploadUtils dataUploadUtils;
	
	@Value("${filestore.host}")
	private String fileStorePath;
	
	@Value("${filestore.get.endpoint}")
	private String getFileEndpoint;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadService.class);
	
	public void getFile(UploaderRequest uploaderRequest){
		StringBuilder uri = new StringBuilder();
		uri.append(fileStorePath).append(getFileEndpoint);
		uri.append("?fileStoreId="+uploaderRequest.getFileStoreId())
		   .append("&tenantId="+uploaderRequest.getTenantId());
		try{
			dataUploadRepository.getFileContents(uri.toString());
		}catch(Exception e){
			logger.error("Exception",e);
		}
		
	}

	public UploaderResponse doInterServiceCall(MultipartFile file, String moduleName,
			String defName, RequestInfo requestInfo) throws Exception {
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
	    Definition uploadDefinition = dataUploadUtils.getUploadDefinition(uploadDefinitionMap, moduleName, defName);
	    List<ResponseMetaData> success = new ArrayList<>();
	    List<ResponseMetaData> failure = new ArrayList<>();
		HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        logger.info("Workbook: "+sheet);
        List<String> jsonPathList = new ArrayList<>();
        List<List<Object>> excelData = dataUploadUtils.readExcelFile(sheet, jsonPathList);
        try{
		    if(uploadDefinition.getIsBulkApi()){
		    	String request = prepareDataForBulkApi(excelData, jsonPathList, uploadDefinition, requestInfo, success, failure);
		        hitApi(request, uploadDefinition.getUri());
				ResponseMetaData responseMetaData= new ResponseMetaData();
				responseMetaData.setRowData(excelData);
	        	success.add(responseMetaData);
		    } else{
		    	callNonBulkApis(excelData, jsonPathList, uploadDefinition, requestInfo, success, failure);
		    }
        }catch(Exception e){
			ResponseMetaData responseMetaData= new ResponseMetaData();
			responseMetaData.setRowData(excelData);
        	failure.add(responseMetaData);
        }
        
        UploaderResponse uploaderResponse = new UploaderResponse();
        uploaderResponse.setSuccess(success);
        uploaderResponse.setFailure(failure);
        
        return uploaderResponse;
	}
	
	private String callNonBulkApis(List<List<Object>> excelData,
			     List<String> jsonPathList, Definition uploadDefinition, RequestInfo requestInfo,
			     List<ResponseMetaData> success, List<ResponseMetaData> failure){
		String request = null;
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
		for(List<Object> row: excelData){
			try{
				if(!row.isEmpty()){
					for(int i = 0; i < jsonPathList.size(); i++){
						StringBuilder expression = new StringBuilder();
				        String key = dataUploadUtils.getJsonPathKey(jsonPathList.get(i), expression);
				        documentContext.put(expression.toString(), key, row.get(i));	            	
					} 	
				    logger.info("RequestInfo: "+requestInfo);
				    try{
				    	documentContext.put("$", "RequestInfo", requestInfo);
					    request = documentContext.jsonString().toString();
				    }catch(Exception e){
				    	documentContext.put("$", "requestInfo", requestInfo);
					    request = documentContext.jsonString().toString();
				    }
				            	
				    hitApi(request, uploadDefinition.getUri());
				            	
				    ResponseMetaData responseMetaData= new ResponseMetaData();
				    responseMetaData.setRownum((excelData.indexOf(row) + 2));
				    responseMetaData.setRowData(row);
				    success.add(responseMetaData);	         	
				}
			}catch(Exception e){
				logger.error("Error while processing row (here row 1 is the first data row after headers): "+(excelData.indexOf(row) + 2), e);
		    	ResponseMetaData responseMetaData= new ResponseMetaData();
		    	responseMetaData.setRownum((excelData.indexOf(row) + 2));
		    	responseMetaData.setRowData(row);
		    	failure.add(responseMetaData);	    			
		    	continue;
		    }
		}
	    
	    return request;
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
}