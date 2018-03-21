package org.egov.pgr.controller;

import java.util.Date;

import javax.validation.Valid;

import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.utils.PGRRequestValidator;
import org.egov.pgr.v2.contract.ServiceRequest;
import org.egov.pgr.v2.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/services/")
public class ServiceController {

	@Autowired
	private GrievanceService service;
	

	@Autowired
	private PGRRequestValidator pgrRequestValidator;

	/**
	 * enpoint to create service requests
	 * 
	 * @param ServiceReqRequest
	 * @author kaviyarasan
	 */
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid ServiceRequest serviceRequest) {

		long startTime = new Date().getTime();
		ServiceResponse response = service.create(serviceRequest);
		long endTime = new Date().getTime();
		log.debug(" the time taken for create in ms: {}", endTime - startTime);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * enpoint to update service requests
	 * 
	 * @param ServiceReqRequest
	 * @author el rey
	 */
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid ServiceRequest serviceRequest) {

		long startTime = new Date().getTime();
		//pgrRequestValidator.validateUpdate(serviceRequest);
		ServiceResponse response = service.update(serviceRequest);
		long endTime = new Date().getTime();
		log.debug(" the time taken for update in ms: {}", endTime - startTime);
		return new ResponseEntity<>(response, HttpStatus.OK);
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

		pgrRequestValidator.validateSearch(serviceReqSearchCriteria, requestInfoWrapper.getRequestInfo());
		long startTime = new Date().getTime();
		Object serviceReqResponse = service.getServiceRequests(requestInfoWrapper.getRequestInfo(),
				serviceReqSearchCriteria);
		long endTime = new Date().getTime();
		log.debug(" the time taken for search in ms: {}", endTime - startTime);
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
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		pgrRequestValidator.validateSearch(serviceReqSearchCriteria, requestInfoWrapper.getRequestInfo());
		long startTime = new Date().getTime();
		Object countResponse = service.getCount(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		long endTime = new Date().getTime();
		log.debug(" the time taken for count in ms: {}", endTime - startTime);
		return new ResponseEntity<>(countResponse, HttpStatus.OK);
	}


}
