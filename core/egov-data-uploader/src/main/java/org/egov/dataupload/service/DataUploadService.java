package org.egov.dataupload.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.egov.DataUploadApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.ResponseMetaData;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.dataupload.model.UploaderResponse;
import org.egov.dataupload.repository.DataUploadRepository;
import org.egov.tracer.model.CustomException;
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
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadService.class);

	public UploaderResponse doInterServiceCall(MultipartFile file, String moduleName, RequestInfo requestInfo) throws Exception {
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
	    Definition uploadDefinition = getUploadDefinition(uploadDefinitionMap, moduleName, file.getName());
	    List<ResponseMetaData> success = new ArrayList<>();
	    List<ResponseMetaData> failure = new ArrayList<>();
		HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        logger.info("Workbook: "+sheet);
        List<String> jsonPathList = new ArrayList<>();
        List<List<Object>> excelData = readExcelFile(sheet, jsonPathList);
        try{
		    if(uploadDefinition.getIsBulkApi()){
		    	String request = prepareDataForBulkApi(excelData, jsonPathList, uploadDefinition, requestInfo, success, failure);
		        hitApi(request, uploadDefinition.getUri());
		    } else{
		    	callNonBulkApis(excelData, jsonPathList, uploadDefinition, requestInfo, success, failure);
		    }
        }catch(Exception e){
			ResponseMetaData responseMetaData= new ResponseMetaData();
			responseMetaData.setRowData(excelData);
        	failure.add(responseMetaData);
        //	throw new CustomException("500", "External Service returned an error");
        }
        
        UploaderResponse uploaderResponse = new UploaderResponse();
        uploaderResponse.setSuccess(success);
        uploaderResponse.setFailure(failure);
        
        return uploaderResponse;
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
			logger.error("There's no Upload Definition provided for this excel file", e);
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
	
	private String callNonBulkApis(List<List<Object>> excelData,
			     List<String> jsonPathList, Definition uploadDefinition, RequestInfo requestInfo,
			     List<ResponseMetaData> success, List<ResponseMetaData> failure){
		String request = null;
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
		for(List<Object> row: excelData){
		    	   try{
					   if(!row.isEmpty()){
				           for(int i = 0; i < jsonPathList.size(); i++){
				                String[] expressionArray = (jsonPathList.get(i)).split("[.]");
				            	StringBuilder expression = new StringBuilder();
				            	for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
				            		expression.append(expressionArray[j]);
				            		if(j != expressionArray.length - 2)
				            			expression.append(".");
				            	}	            		
				            	documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1], 
				            			row.get(i));	            	
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
				            	
					   }else{
						   continue;
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
	    		try{
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
			String[] expressionArray = (uploadDefinition.getArrayPath()).split("[.]");
			StringBuilder expression = new StringBuilder();
			for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
				expression.append(expressionArray[j]);
				if(j != expressionArray.length - 2)
					expression.append(".");
			}
		    Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
			Gson gson = new Gson();
			List<Map<String, Object>> array = gson.fromJson(dataList.toString(), type);
			documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1], array);	 
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
	
	private List<List<Object>> readExcelFile(HSSFSheet sheet, List<String> jsonPathList){
        List<List<Object>> excelData = new ArrayList<>(); 
        Iterator<Row> iterator = sheet.iterator();
        while(iterator.hasNext()){
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            List<Object> dataList = new ArrayList<>();
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

        
        return excelData;

		
	}
		
	private Object hitApi(String request, String uri){
		Object response = null;
		logger.info("Request: "+request);
    	logger.info("uri: "+uri);
    	
    	response = dataUploadRepository.doApiCall(request, uri);
    	
    	return response;
		
	}
}