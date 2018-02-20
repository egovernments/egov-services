package org.egov.pgr.controller;

import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.service.ServiceRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/requests/")
public class ServiceRequestController {
	
	public static final Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);

	@Autowired
	private ServiceRequestService service;
	

	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody ServiceReqRequest serviceRequest) {

		ServiceReqResponse response = service.create(serviceRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("_search")
	@ResponseBody
	private ResponseEntity<?> search(@RequestBody RequestInfoWrapper requestInfoWrapper, 
			@ModelAttribute ServiceReqSearchCriteria serviceReqSearchCriteria) throws Exception{
		logger.info("RequestInfo: "+requestInfoWrapper.toString());
		logger.info("ServiceReqSearchCriteria: "+serviceReqSearchCriteria.toString());
		ServiceReqResponse serviceReqResponse = service.getServiceRequests(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);	
	}

	@PostMapping("count/_search")
	@ResponseBody
	private ResponseEntity<?> count() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
