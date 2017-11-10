package org.egov.dataupload.service;

import java.io.IOException;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Service
public class DataUploadService {

	@Autowired
	private DataUploadRepository searchRepository;
	
	@Autowired
	private DataUploadApplicationRunnerImpl runner;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadService.class);


	@SuppressWarnings("deprecation")
	public Object buildRequest(MultipartFile file, String moduleName, RequestInfo requestInfo) throws IOException {
		Map<String, UploadDefinition> uploadDefinitionMap = runner.getUploadDefinitionMap();
	    Definition uploadDefinition = getUploadDefinition(uploadDefinitionMap, moduleName, file.getName());
		HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        logger.info("Workbook: "+sheet);
        HSSFRow row;
        List<String> jsonPathList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        Iterator<Row> iterator = sheet.iterator();
    	DocumentContext documentContext = JsonPath.parse(uploadDefinition.getApiRequest());
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
	            
	            if(!uploadDefinition.getIsBulkApi()){
	            	for(int i = 0; i < jsonPathList.size(); i++){
	            		String[] expressionArray = (jsonPathList.get(i)).split("[.]");
	            		StringBuilder expression = new StringBuilder();
	            		for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
	            			expression.append(expressionArray[i]);
	            			if(j != expressionArray.length - 2)
	            				expression.append(".");
	            		}
	            		documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1], 
	            				dataList.get(i));	            	
	            		}
	            	documentContext.put("$", "RequestInfo", requestInfo);
	            	
	            	logger.info("Request: "+documentContext.jsonString().toString());
	            	
	            }
            }
            
            String apiRequest = null;
            
            
        }
        
        return null;
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
			logger.error("There's no Uploaded Definition provided for this excel file");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this excel file");
		}
		return definitions.get(0);
		
	}
}
