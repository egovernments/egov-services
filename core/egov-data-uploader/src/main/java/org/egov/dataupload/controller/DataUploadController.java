package org.egov.dataupload.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.model.UploaderResponse;
import org.egov.dataupload.service.DataUploadService;
import org.egov.dataupload.utils.ResponseInfoFactory;
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


@RestController
public class DataUploadController {
		
	@Autowired
	private DataUploadService dataUploadService;
		
	@Autowired
    public static ResourceLoader resourceLoader;
	
	@Autowired
	public ResponseInfoFactory responseInfoFactory;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadController.class);
	
	@PostMapping("/{moduleName}/{defName}/_upload")
	@ResponseBody
	public ResponseEntity<?> getReportData(@RequestParam("file") MultipartFile inputFile,
			/*@RequestPart("file") MultipartFile inputFile,
			@RequestPart(value="RequestInfo", required=false) RequestInfo requestInfo,*/
			@PathVariable("moduleName") String moduleName,
			@PathVariable("defName") String defName) throws Exception {
		try {
				logger.info("Inside controller");
				RequestInfo requestInfo = RequestInfo.builder().action("create").apiId("dataup").authToken("867ab332-5a3e-4b13-8c71-338bfeb80e44")
				.did("1").msgId("20170310130900").ts(10032017L).build();
				UploaderResponse result = dataUploadService.doInterServiceCall(inputFile, moduleName, defName, requestInfo);
				result.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
				return new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e){
			throw e;
		}
	}

		
}