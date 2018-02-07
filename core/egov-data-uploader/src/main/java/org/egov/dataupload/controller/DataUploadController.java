package org.egov.dataupload.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.dataupload.model.JobSearchRequest;
import org.egov.dataupload.model.ModuleDefRequest;
import org.egov.dataupload.model.ModuleDefResponse;
import org.egov.dataupload.model.ModuleDefs;
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
	
	
	@PostMapping("jobs/_create")
	@ResponseBody
	public ResponseEntity<?> upload(@RequestBody @Valid UploaderRequest uploaderRequest) throws Exception {
		try {
				logger.info("Inside controller");
				List<UploadJob> uploadJobs = dataUploadService.createUploadJob(uploaderRequest);
				UploaderResponse result = UploaderResponse.builder()
						.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(uploaderRequest.getRequestInfo(), true))
						.uploadJobs(uploadJobs).build();
				return new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e){
			throw e;
		}
	}
	
	@PostMapping("upload-definitions/_search")
	@ResponseBody
	public ResponseEntity<?> definitionSearch(@RequestBody @Valid ModuleDefRequest moduleDefRequest) throws Exception {
		try {
				logger.info("Inside controller");
				List<ModuleDefs> moduleDefs = dataUploadService.getModuleDefs();
				ModuleDefResponse result = ModuleDefResponse.builder()
						.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(moduleDefRequest.getRequestInfo(), true))
						.moduleDefs(moduleDefs).build();
				return new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e){
			throw e;
		}
	}
	
	@PostMapping("jobs/_search")
	@ResponseBody
	public ResponseEntity<?> jobsSearch(@RequestBody @Valid JobSearchRequest jobSearchRequest) throws Exception {
		try {
				logger.info("Inside controller");
				List<UploadJob> uploadJobs = dataUploadService.getUploadJobs(jobSearchRequest);
				UploaderResponse result = UploaderResponse.builder()
						.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(jobSearchRequest.getRequestInfo(), true))
						.uploadJobs(uploadJobs).build();
				return new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e){
			throw e;
		}
	}
}

		