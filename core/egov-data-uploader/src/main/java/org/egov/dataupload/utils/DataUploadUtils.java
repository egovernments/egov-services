package org.egov.dataupload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;


@Component
public class DataUploadUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadUtils.class);
	
	@Value("${internal.file.folder.path}")
	private String internalFolderPath;
	
	@Value("${business.module.host}")
	private String businessModuleHost;
	
	@SuppressWarnings({ "deprecation", "static-access" })
	public List<List<Object>> readExcelFile(HSSFSheet sheet, List<Object> coloumnHeaders){
        List<List<Object>> excelData = new ArrayList<>(); 
        Iterator<Row> iterator = sheet.iterator();
        while(iterator.hasNext()){
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            List<Object> dataList = new ArrayList<>();
            int i = 0;
            int j = 0;
            while(cellIterator.hasNext()){
	            Cell cell = cellIterator.next();
	            if(0 == cell.getRowIndex()) {
	            	coloumnHeaders.add(cell.getStringCellValue());
	            	j++;
	            }
	            else{
		            if(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
		            	if(cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
		            	    if (HSSFDateUtil.isCellDateFormatted(cell)) {
			            		logger.info("Adding Date val: "+cell.getDateCellValue());
			            		logger.info("Epoch of it: "+cell.getDateCellValue().getTime());
			            		dataList.add(cell.getDateCellValue().getTime());
		            	    }else {
		            	    	dataList.add(cell.getNumericCellValue());
		            	    }
		            	}
		            	else if(cell.CELL_TYPE_STRING == cell.getCellType()) {
		            		if(null != cell.getStringCellValue() || !cell.getStringCellValue().isEmpty()) {
		            			dataList.add(cell.getStringCellValue());
		            		}
		            		else {
			            		dataList.add(null);
		            		}
		            	}
		            	else if(cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
		            		dataList.add(cell.getBooleanCellValue());

		            	}
		            }else {
		            	dataList.add(null);
		            }
	            	
	            }
            }
            if(!dataList.isEmpty())
            	excelData.add(dataList);
        }
	    logger.info("coloumnHeaders: "+coloumnHeaders);
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
	
	public MultipartFile getExcelFile(String path) throws Exception{
		File file = new File(path);
	    FileInputStream input = new FileInputStream(file);
	    MultipartFile multipartFile = new MockMultipartFile("ReadFile",
	            file.getName(), "text/plain", IOUtils.toByteArray(input));
	    
	    return multipartFile;
	}
	
	public String createANewFile(String fileName) {
		String folder = internalFolderPath;
		logger.info("Creating a new file: "+fileName);
		logger.info("In the internal folder: "+folder);
		try {
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("Sheet 1"); 
	        folder = folder + "/"+ fileName;
	        FileOutputStream fileOut = new FileOutputStream(folder);
	        workbook.write(fileOut);
	        fileOut.close();
	        workbook.close();
		}catch(Exception e) {
			logger.error("New file creation for processing failed",e);
		}
        
        return folder;
		
	}
	
	
	public void writeToexcelSheet(List<Object> exisitingFields, String fileName) throws Exception{
		logger.info("Writing to file: "+fileName);
	    MultipartFile file = getExcelFile(fileName);
		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        Row row = null;
        row = sheet.createRow(++rowCount);
        for(int i = 0; i < exisitingFields.size(); i++)
        {
            Cell cell = row.createCell(i);
            if(exisitingFields.get(i) instanceof String){
            	cell.setCellType(CellType.STRING);
            	cell.setCellValue(exisitingFields.get(i).toString());
            }else if(exisitingFields.get(i) instanceof Double){
            	cell.setCellType(CellType.NUMERIC);
            	cell.setCellValue(Double.parseDouble(exisitingFields.get(i).toString()));
            }
            
        }
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        }
          
        workbook.close();
	}

	public void clearInternalDirectory(){
		logger.info("Clearing the internal folder....: "+internalFolderPath);
		try{
			FileUtils.cleanDirectory(new File(internalFolderPath)); 
		}catch(Exception e){
			logger.error("Couldn't clean the folder: "+internalFolderPath, e);
		}
        
	}
	
	
	public List<Object> getResJsonPathList(Map<String, String> resFieldsMap, List<Object> coloumnHeaders){
		List<Object> jsonpathList = new ArrayList<>();
		for(Entry<String, String> entry: resFieldsMap.entrySet()){
			coloumnHeaders.add(entry.getValue());
			jsonpathList.add(entry.getKey());
		}
		
		return jsonpathList;
		
	}
	
	public void addAdditionalFields(Object response, List<Object> row, List<Object> jsonPathList) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		response = mapper.writeValueAsString(response);
		for(Object path: jsonPathList){
			try{
				Object value = JsonPath.read(response, path.toString());
				row.add(value);
			}catch(Exception e){
				row.add(null);
				
				continue;
			}
		}
	}
	
	public String mockIdGen(String module, String defName){
		StringBuilder id = new StringBuilder();
		id.append(module+"-").append(defName+"-").append(new Date().getTime());
		logger.info("JOB CODE: "+id.toString());
		return id.toString();
	}

	public String getURI(String endPoint){
		logger.info("endpoint: "+endPoint);
		StringBuilder uri = new StringBuilder();
		uri.append(businessModuleHost).append(endPoint);
		
		return uri.toString();

	}
	
	public List<List<Object>> filter(List<List<Object>> excelData, List<Integer> indexes, List<Object> row){
		List<List<Object>> result = null;
		for(Integer index: indexes){
			result = excelData.parallelStream()
					.filter(obj -> ((obj.get(index)).equals(row.get(index))))
					.collect(Collectors.toList());
			
			excelData = result;
		}
		return result;
	}
}


