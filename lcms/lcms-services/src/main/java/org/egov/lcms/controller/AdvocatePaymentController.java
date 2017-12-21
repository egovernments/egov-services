package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocatePaymentRequest;
import org.egov.lcms.models.AdvocatePaymentResponse;
import org.egov.lcms.models.AdvocatePaymentSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.service.AdvocatePaymentService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Shubham		28th Oct 2107						Initial commit for advocate payment controller 
* Prasad		30th Oct 2017						Changed requestInfo to requestInfoWrapper
* Prasad		03rd Nov 2017						Added throws statement for searchAdvocatePayment
*/
@RestController
@RequestMapping("/legalcase/advocatepayment")
public class AdvocatePaymentController {

	@Autowired
	AdvocatePaymentService advocatePaymentService;

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	/**
	 * API for AdvocatePayement create
	 * 
	 * @param advocatePaymentRequest
	 * @return AdvocatePaymentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createAdvocatePayment(@RequestBody @Valid AdvocatePaymentRequest advocatePaymentRequest)
			throws Exception {

		AdvocatePaymentResponse advocatePaymentResponse = advocatePaymentService
				.createAdvocatePayment(advocatePaymentRequest);

		return new ResponseEntity<>(advocatePaymentResponse, HttpStatus.CREATED);
	}

	/**
	 * API for AdvocatePayment update
	 * 
	 * @param advocatePaymentRequest
	 * @return AdvocatePaymentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateAdvocatePayment(@RequestBody @Valid AdvocatePaymentRequest advocatePaymentRequest)
			throws Exception {

		AdvocatePaymentResponse advocatePaymentResponse = advocatePaymentService
				.updateAdvocatePayment(advocatePaymentRequest);

		return new ResponseEntity<>(advocatePaymentResponse, HttpStatus.CREATED);
	}

	/**
	 * Search API for AdvocatePayment service.
	 * 
	 * @param requestInfo
	 * @param advocatePaymentSearchCriteria
	 * @return AdvocatePaymentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public ResponseEntity<?> searchAdvocatePayment(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria) throws Exception {

		if (advocatePaymentSearchCriteria.getTenantId() == null) {
			throw new CustomException(propertiesManager.getTenantMandatoryCode(),
					propertiesManager.getTenantMandatoryMessage());
		}
		return new ResponseEntity<>(advocatePaymentService.searchAdvocatePayment(requestInfoWrapper.getRequestInfo(),
				advocatePaymentSearchCriteria), HttpStatus.OK);
	}
}
