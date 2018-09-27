package org.egov.tlcalculator.web.controllers;

import javax.validation.Valid;

import org.egov.tlcalculator.web.models.BillingSlab;
import org.egov.tlcalculator.web.models.BillingSlabReq;
import org.egov.tlcalculator.web.models.BillingSlabRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tl-calculator/")
public class BillingslabController {
	
	@RequestMapping(value = "/billingslab/_create", method = RequestMethod.POST)
	public ResponseEntity<BillingSlabRes> billingslabCreatePost(@Valid @RequestBody BillingSlabReq billingSlab) {

		return new ResponseEntity<BillingSlabRes>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@RequestMapping(value = "/billingslab/_update", method = RequestMethod.POST)
	public ResponseEntity<BillingSlab> billingslabUpdatePost(@Valid @RequestBody BillingSlabReq billingSlab) {

		return new ResponseEntity<BillingSlab>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/billingslab/_search", method = RequestMethod.POST)
	public ResponseEntity<BillingSlabRes> billingslabSearchPost() {
		return new ResponseEntity<BillingSlabRes>(HttpStatus.NOT_IMPLEMENTED);
	}

}
