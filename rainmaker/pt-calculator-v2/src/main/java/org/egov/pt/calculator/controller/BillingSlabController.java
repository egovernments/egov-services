package org.egov.pt.calculator.controller;

import java.util.Date;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.calculator.service.BillingSlabService;
import org.egov.pt.calculator.validator.BillingSlabValidator;
import org.egov.pt.calculator.web.models.BillingSlabReq;
import org.egov.pt.calculator.web.models.BillingSlabRes;
import org.egov.pt.calculator.web.models.BillingSlabSearchCriteria;
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
@RequestMapping("/billing-slab")
@Slf4j
public class BillingSlabController {
	
	
	@Autowired
	private BillingSlabService service;
	
	@Autowired
	private BillingSlabValidator billingSlabValidator;
	

	/**
	 * enpoint to create billing-slabs
	 * 
	 * @param BillingSlabReq
	 * @author vishal
	 */
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid BillingSlabReq billingSlabReq) {

		long startTime = new Date().getTime();
		billingSlabValidator.validateCreate(billingSlabReq);
		BillingSlabRes billingSlabRes = service.createBillingSlab(billingSlabReq);
		long endTime = new Date().getTime();
		log.debug(" the time taken for create in ms: {}", endTime - startTime);
		return new ResponseEntity<>(billingSlabRes, HttpStatus.CREATED);
	}
	
	/**
	 * enpoint to update billing-slabs
	 * 
	 * @param BillingSlabReq
	 * @author vishal
	 */
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid BillingSlabReq billingSlabReq) {

		long startTime = new Date().getTime();
		billingSlabValidator.validateUpdate(billingSlabReq);
		BillingSlabRes billingSlabRes = service.updateBillingSlab(billingSlabReq);
		long endTime = new Date().getTime();
		log.debug(" the time taken for create in ms: {}", endTime - startTime);
		return new ResponseEntity<>(billingSlabRes, HttpStatus.CREATED);
	}
	
	/**
	 * enpoint to search billing-slabs
	 * 
	 * @param RequestInfo
	 * @param BillingSlabSearchCriteria
	 * @author vishal
	 */
	@PostMapping("_search")
	@ResponseBody
	private ResponseEntity<?> search(@RequestBody @Valid RequestInfo requestInfo, 
					@ModelAttribute @Valid BillingSlabSearchCriteria billingSlabSearcCriteria) {
		long startTime = new Date().getTime();
		BillingSlabRes billingSlabRes = service.searchBillingSlabs(requestInfo, billingSlabSearcCriteria);
		long endTime = new Date().getTime();
		log.debug(" the time taken for create in ms: {}", endTime - startTime);
		return new ResponseEntity<>(billingSlabRes, HttpStatus.CREATED);
	}
}
