package org.egov.dataupload.controller;

import java.lang.reflect.Type;
import java.util.Map;


import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.model.UploaderResponse;
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
	
	@PostMapping("/{moduleName}/_upload")
	@ResponseBody
	public ResponseEntity<?> getReportData(@RequestParam("file") MultipartFile inputFile,
			/*@RequestPart("file") MultipartFile inputFile,
			@RequestPart(value="RequestInfo", required=false) RequestInfo requestInfo,*/
			@PathVariable("moduleName") String moduleName) throws Exception {
		try {
				logger.info("Inside controller");
				RequestInfo requestInfo = RequestInfo.builder().action("create").apiId("dataup").authToken("b6ae3ec1-429e-4400-b823-225fdc71f1b0")
				.did("1").msgId("20170310130900").ts(10032017L).build();
				UploaderResponse result = dataUploadService.doInterServiceCall(inputFile, moduleName, requestInfo);
			   /* Type type = new TypeToken<Map<String, Object>>() {}.getType();
				Gson gson = new Gson();
		    	Map<String, Object> data = gson.fromJson(result.toString(), type); */
				return new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e){
			throw e;
		}
	}

		
}