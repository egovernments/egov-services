package org.egov.tlcalculator.web.controllers;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.service.BillingslabService;
import org.egov.tlcalculator.validator.BillingslabValidator;
import org.egov.tlcalculator.web.models.BillingSlabReq;
import org.egov.tlcalculator.web.models.BillingSlabRes;
import org.egov.tlcalculator.web.models.BillingSlabSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/billingslab")
public class BillingslabController {
	
	@Autowired
	private BillingslabValidator billingslabValidator;
	
	@Autowired
	private BillingslabService service;
	
	@RequestMapping(value = "/_create", method = RequestMethod.POST)
	public ResponseEntity<BillingSlabRes> billingslabCreatePost(@Valid @RequestBody BillingSlabReq billingSlabReq) {
		billingslabValidator.validateCreate(billingSlabReq);
		BillingSlabRes response = service.createSlabs(billingSlabReq);
		return new ResponseEntity<BillingSlabRes>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/_update", method = RequestMethod.POST)
	public ResponseEntity<BillingSlabRes> billingslabUpdatePost(@Valid @RequestBody BillingSlabReq billingSlabReq) {
		billingslabValidator.validateUpdate(billingSlabReq);
		BillingSlabRes response = service.updateSlabs(billingSlabReq);
		return new ResponseEntity<BillingSlabRes>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/_search", method = RequestMethod.POST)
	public ResponseEntity<BillingSlabRes> billingslabSearchPost(@ModelAttribute @Valid BillingSlabSearchCriteria billingSlabSearchCriteria,
			@Valid @RequestBody RequestInfo requestInfo) {
		BillingSlabRes response = service.searchSlabs(billingSlabSearchCriteria, requestInfo);
		return new ResponseEntity<BillingSlabRes>(response, HttpStatus.OK);
	}

}
