package org.egov.pgr.controller;

import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.service.ServiceRequestService;
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
	private ResponseEntity<?> update(@RequestBody ServiceReqRequest serviceRequest) {
		
		ServiceReqResponse response = service.update(serviceRequest);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	/**
	 * Controller enpoint to fetch service requests
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return
	 * @throws Exception
	 * @author vishal
	 */
	@PostMapping("_search")
	@ResponseBody
	private ResponseEntity<?> search(@RequestBody RequestInfoWrapper requestInfoWrapper, 
			@ModelAttribute ServiceReqSearchCriteria serviceReqSearchCriteria) throws Exception{
		log.info("RequestInfo: ",requestInfoWrapper.toString());
		log.info("ServiceReqSearchCriteria: ",serviceReqSearchCriteria.toString());
		ServiceReqResponse serviceReqResponse = service.getServiceRequests(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);	
	}

	@PostMapping("_count")
	@ResponseBody
	private ResponseEntity<?> count(@RequestBody RequestInfoWrapper requestInfoWrapper, 
			@ModelAttribute ServiceReqSearchCriteria serviceReqSearchCriteria) throws Exception{
		log.info("RequestInfo: ",requestInfoWrapper.toString());
		log.info("CountCriteria: ",serviceReqSearchCriteria.toString());
		Object countResponse = service.getCount(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		return new ResponseEntity<>(countResponse, HttpStatus.OK);	
	}


}
