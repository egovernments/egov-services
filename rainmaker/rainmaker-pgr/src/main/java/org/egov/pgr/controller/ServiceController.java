package org.egov.pgr.controller;

import java.util.Date;

import javax.validation.Valid;

import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.v2.contract.ServiceRequest;
import org.egov.pgr.v2.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

}
