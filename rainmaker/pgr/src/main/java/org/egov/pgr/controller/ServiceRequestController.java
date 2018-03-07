package org.egov.pgr.controller;

import javax.validation.Valid;

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
	
	
	/**
	 *  enpoint to create service requests
	 * 
	 * @param ServiceReqRequest
	 * @author kaviyarasan
	 */
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid ServiceReqRequest serviceRequest) {

		ServiceReqResponse response = service.create(serviceRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 *  enpoint to update service requests
	 * 
	 * @param ServiceReqRequest
	 * @author el rey
	 */
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid ServiceReqRequest serviceRequest) {
		
		ServiceReqResponse response = service.update(serviceRequest);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	/**
	 * Controller endpoint to fetch service requests
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * @author vishal
	 */
	@PostMapping("_search")
	@ResponseBody
	private ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper, 
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		
		log.debug("RequestInfo: ",requestInfoWrapper.toString());
		log.debug("ServiceReqSearchCriteria: ",serviceReqSearchCriteria.toString());
		ServiceReqResponse serviceReqResponse = service.getServiceRequests(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);	
	}
	
	/**
	 * Controller to fetch count of service requests based on a given criteria
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * @author vishal
	 */
	@PostMapping("_count")
	@ResponseBody
	private ResponseEntity<?> count(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper, 
			@ModelAttribute ServiceReqSearchCriteria serviceReqSearchCriteria) {
		
		log.debug("RequestInfo: ",requestInfoWrapper.toString());
		log.debug("CountCriteria: ",serviceReqSearchCriteria.toString());
		Object countResponse = service.getCount(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		return new ResponseEntity<>(countResponse, HttpStatus.OK);	
	}


}
