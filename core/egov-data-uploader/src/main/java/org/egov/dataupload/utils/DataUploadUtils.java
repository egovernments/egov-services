package org.egov.dataupload.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class DataUploadUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadUtils.class);
	
	public List<List<Object>> readExcelFile(HSSFSheet sheet, List<String> jsonPathList){
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
	
	public Definition getUploadDefinition(Map<String, UploadDefinition> searchDefinitionMap,
			String moduleName, String defName){
		logger.info("Fetching Definitions for module: "+moduleName+" and upload feature: "+defName);
		List<Definition> definitions = null;
		try{
			definitions = searchDefinitionMap.get(moduleName).getDefinitions().parallelStream()
											.filter(def -> (def.getDefName().equals(defName)))
		                                 .collect(Collectors.toList());
		}catch(Exception e){
			logger.error("There's no Upload Definition provided for this upload feature", e);
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this upload feature");
		}
		if(0 == definitions.size()){
			logger.error("There's no Upload Definition provided for this upload feature");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this upload feature");
		}
		
		logger.info("Definition to be used: "+definitions.get(0));

		return definitions.get(0);
		
	}
	
	public String getJsonPathKey(String jsonPath, StringBuilder expression){
        String[] expressionArray = (jsonPath).split("[.]");
    	for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
    		expression.append(expressionArray[j]);
    		if(j != expressionArray.length - 2)
    			expression.append(".");
    	}
    	return expressionArray[expressionArray.length - 1];
	}

}
