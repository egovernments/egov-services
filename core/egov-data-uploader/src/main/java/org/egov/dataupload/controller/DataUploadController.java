package org.egov.dataupload.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.dataupload.model.UploadJob;
import org.egov.dataupload.model.UploaderRequest;
import org.egov.dataupload.model.UploaderResponse;
import org.egov.dataupload.service.DataUploadService;
import org.egov.dataupload.utils.ResponseInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1")
public class DataUploadController {
		
	@Autowired
	private DataUploadService dataUploadService;
		
	@Autowired
    public static ResourceLoader resourceLoader;
	
	@Autowired
	public ResponseInfoFactory responseInfoFactory;
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadController.class);
	
	
	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> upload(@RequestBody @Valid UploaderRequest uploaderRequest) throws Exception {
		try {
				logger.info("Inside controller");
				RequestInfo requestInfo = RequestInfo.builder().action("create").apiId("dataup").authToken("867ab332-5a3e-4b13-8c71-338bfeb80e44")
				.did("1").msgId("20170310130900").ts(10032017L).build();
				List<UploadJob> uploadJobs = dataUploadService.createUploadJob(uploaderRequest);
				UploaderResponse result = UploaderResponse.builder()
						.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true))
						.uploadJobs(uploadJobs).build();
				return new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e){
			throw e;
		}
	}
}

		