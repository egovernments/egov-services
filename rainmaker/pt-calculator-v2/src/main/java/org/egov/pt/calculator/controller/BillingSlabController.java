package org.egov.pt.calculator.controller;

import java.util.Date;

import javax.validation.Valid;

import org.egov.pt.calculator.service.BillingSlabService;
import org.egov.pt.calculator.web.models.property.BillingSlabReq;
import org.egov.pt.calculator.web.models.property.BillingSlabRes;
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
@RequestMapping("/billing-slab")
@Slf4j
public class BillingSlabController {
	
	
	@Autowired
	private BillingSlabService service;
	

	/**
	 * enpoint to create service requests
	 * 
	 * @param ServiceReqRequest
	 * @author vishal
	 */
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid BillingSlabReq billingSlabReq) {

		//pgrRequestValidator.validateCreate(serviceRequest);
		long startTime = new Date().getTime();
		BillingSlabRes billingSlabRes = service.createBillingSlab(billingSlabReq);
		long endTime = new Date().getTime();
		log.debug(" the time taken for create in ms: {}", endTime - startTime);
		return new ResponseEntity<>(billingSlabRes, HttpStatus.CREATED);
	}
	
	/**
	 * enpoint to create service requests
	 * 
	 * @param ServiceReqRequest
	 * @author vishal
	 */
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid BillingSlabReq billingSlabReq) {

		//pgrRequestValidator.validateCreate(serviceRequest);
		long startTime = new Date().getTime();
		BillingSlabRes billingSlabRes = service.updateBillingSlab(billingSlabReq);
		long endTime = new Date().getTime();
		log.debug(" the time taken for create in ms: {}", endTime - startTime);
		return new ResponseEntity<>(billingSlabRes, HttpStatus.CREATED);
	}
}
