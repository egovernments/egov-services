package org.egov.dataupload.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.egov.DataUploadApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.ResponseInfoFactory;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.dataupload.repository.DataUploadRepository;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private ResponseInfoFactory responseInfoFactory;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadService.class);


	@SuppressWarnings("deprecation")
	public Object buildRequest(MultipartFile file, String moduleName, RequestInfo requestInfo) throws IOException {
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
	    Definition uploadDefinition = getUploadDefinition(uploadDefinitionMap, moduleName, file.getName());
	    Object response = null;
		HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        logger.info("Workbook: "+sheet);
        List<String> jsonPathList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        List<List<Object>> excelData = new ArrayList<>(); 
        Iterator<Row> iterator = sheet.iterator();
        while(iterator.hasNext()){
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            while(cellIterator.hasNext()){
	            Cell cell = cellIterator.next();
	            if(0 == cell.getRowIndex())
	            	jsonPathList.add(cell.getStringCellValue());
	            else{
	            	dataList.add(cell.getStringCellValue());
	            }
            }
            excelData.add(dataList);
        }
	    logger.info("jsonPathList: "+jsonPathList);
	    logger.info("excelData: "+excelData);
	    
	    if(uploadDefinition.getIsBulkApi()){
	    	String request = prepareDataForBulkApi(excelData, jsonPathList, uploadDefinition, requestInfo);
	    	response = hitApi(request, uploadDefinition.getUri());
	    } else{
	    	String request = prepareDataForNonBulkApi(excelData, jsonPathList, uploadDefinition, requestInfo);
	    	response = hitApi(request, uploadDefinition.getUri());
	    }
	    
        
        return response;
	}
	
	private Definition getUploadDefinition(Map<String, UploadDefinition> searchDefinitionMap,
			String moduleName, String fileName){
		logger.info("Fetching Definitions for module: "+moduleName+" and search feature: "+fileName);
		List<Definition> definitions = null;
		try{
			definitions = searchDefinitionMap.get(moduleName).getDefinitions().parallelStream()
											.filter(def -> (def.getFileName().equals(fileName)))
		                                 .collect(Collectors.toList());
		}catch(Exception e){
			logger.error("There's no Upload Definition provided for this excel file");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this excel file");
		}
		if(0 == definitions.size()){
			logger.error("There's no Upload Definition provided for this excel file");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this excel file");
		}
		
		logger.info("Definition to be used: "+definitions.get(0));

		return definitions.get(0);
		
	}
	
	private String prepareDataForNonBulkApi(List<List<Object>> excelData,
			     List<String> jsonPathList, Definition uploadDefinition, RequestInfo requestInfo){
		List<Object> list = excelData.get(0);
		String request = null;
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
	    if(!list.isEmpty()){
	           for(int i = 0; i < jsonPathList.size(); i++){
	                String[] expressionArray = (jsonPathList.get(i)).split("[.]");
	            	StringBuilder expression = new StringBuilder();
	            	for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
	            		expression.append(expressionArray[j]);
	            		if(j != expressionArray.length - 2)
	            			expression.append(".");
	            	}
	            		
	            	logger.info("expression: "+expression.toString());
	            	logger.info("key: "+expressionArray[expressionArray.length - 1]);
	            	logger.info("value: "+list.get(i));
	            		
	            	documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1], 
	            				list.get(i));	            	
	            	}
	            	
	            	logger.info("RequestInfo: "+requestInfo);
	            	documentContext.put("$", "RequestInfo", requestInfo);
	            	request = documentContext.jsonString().toString();	            	
            }else{
            	logger.info("The excel sheet is empty");
            }
	    
	    return request;
	}
	
	private String prepareDataForBulkApi(List<List<Object>> excelData,
		     List<String> jsonPathList, Definition uploadDefinition, RequestInfo requestInfo) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
    	StringBuilder dataArray = new StringBuilder();
    	dataArray.append("[");
    	for(List<Object> list: excelData){
	    	StringBuilder object = new StringBuilder(); 
	    	object.append(JsonPath.read(uploadDefinition.getApiRequest(), uploadDefinition.getArrayPath()).toString());
	    	object.deleteCharAt(0).deleteCharAt(object.toString().length() - 1);
	    	String json = mapper.writeValueAsString(JsonPath.read(uploadDefinition.getApiRequest(), uploadDefinition.getArrayPath()));
	    	json = json.substring(1, json.length() - 1);
	    	logger.info("json: "+json);
	    	DocumentContext docContext = JsonPath.parse(json);
	    	for(int i = 0; i < jsonPathList.size(); i++){
	    		String[] expressionArray = (jsonPathList.get(i)).split("[.]");
	    		StringBuilder expression = new StringBuilder();
	    		for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
	    			expression.append(expressionArray[j]);
	    			if(j != expressionArray.length - 2)
	    				expression.append(".");
	    		}            	
	    		docContext.put("$", expressionArray[expressionArray.length - 1], 
	    				list.get(i));	            	
	    		}
	    	dataArray.append(docContext.jsonString().toString())
	    	         .append(",");
    	}
	    dataArray.deleteCharAt(dataArray.length() - 1).append("]");
	    String data = dataArray.toString();
	    logger.info("Json array of excel sheet: "+data);
	    
		String[] expressionArray = (uploadDefinition.getArrayPath()).split("[.]");
		StringBuilder expression = new StringBuilder();
		for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
			expression.append(expressionArray[j]);
			if(j != expressionArray.length - 2)
				expression.append(".");
		}
		JSONArray jsonArray = new JSONArray(data);
	    Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
		Gson gson = new Gson();
		List<Map<String, Object>> array = gson.fromJson(Arrays.asList(jsonArray).toString(), type);
		documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1], Arrays.asList(jsonArray).toString());	 
    	documentContext.put("$", "RequestInfo", requestInfo);

    	return documentContext.jsonString().toString();
    	
	}
		
	private Object hitApi(String request, String uri){
		Object response = null;
		logger.info("Request: "+request);
    	logger.info("uri: "+uri);
    	
    	response = dataUploadRepository.doApiCall(request, uri);
    	
    	return response;
		
	}
}