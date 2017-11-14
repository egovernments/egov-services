package org.egov.dataupload.controller;

import java.lang.reflect.Type;
import java.util.Map;


import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.service.DataUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
public class DataUploadController {
		
	@Autowired
	private DataUploadService dataUploadService;
		
	@Autowired
    public static ResourceLoader resourceLoader;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadController.class);

	/* @RequestMapping(value = {"/{moduleName}/_get"}, method = RequestMethod.POST, 
	        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}) */
	
	@PostMapping("/{moduleName}/_get")
	@ResponseBody
	public ResponseEntity<?> getReportData(@RequestParam("file") MultipartFile inputFile,
			/*@RequestPart("file") MultipartFile inputFile,
			@RequestPart("RequestInfo") RequestInfo requestInfo, */
			@PathVariable("moduleName") String moduleName) throws Exception {
		try {
			logger.info("Inside controller");
			Object result = dataUploadService.buildRequest(inputFile, moduleName, new RequestInfo());
		    Type type = new TypeToken<Map<String, Object>>() {}.getType();
			Gson gson = new Gson();
			Map<String, Object> data = gson.fromJson(result.toString(), type);
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch(Exception e){
			logger.error("Exception while searching for result: ",e);
			throw e;
		}
	}

		
}