package org.egov.mr.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.service.MarriageCertService;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.egov.mr.web.contract.ReissueCertResponse;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/certs/reissueAppl")
public class MarriageCertController {

	@Autowired
	private MarriageCertService marriageCertService;

	@Autowired
	private ErrorHandler errorHandler;
	
	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid MarriageCertCriteria marriageCertCriteria,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		log.info("marriageCertCriteria: " + marriageCertCriteria.getTenantId());
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForSearch(requestInfo,
				requestBodyBindingResult, null);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		ReissueCertResponse reissueCertResponse = null;
		try {
			reissueCertResponse = marriageCertService.getMarriageCerts(marriageCertCriteria, requestInfo);
			log.debug("marriageCertList;;;;" + reissueCertResponse);
		} catch (Exception exception) {
			log.error(" Error While Procssing the Request!");
			return errorHandler.getUnExpectedErrorResponse(requestBodyBindingResult, requestInfo,
					"Encountered : " + exception.getMessage());
		}
		return new ResponseEntity<ReissueCertResponse>(reissueCertResponse, HttpStatus.OK);
	}
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final ReissueCertRequest reissueCertRequest,
			final BindingResult bindingResult) {
	
		RequestInfo requestInfo = reissueCertRequest.getRequestInfo();
		
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForCreate(requestInfo,
				bindingResult);

		if (errorResponseEntity != null)
			return errorResponseEntity;
		log.info("Request in controller::"+reissueCertRequest);
		ReissueCertResponse reIssueCertAppResponse=marriageCertService.createAsync(reissueCertRequest);
		
		return new ResponseEntity<>(reIssueCertAppResponse, HttpStatus.CREATED);
		
	}
	
	@PostMapping("_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final ReissueCertRequest reissueCertRequest,
			final BindingResult bindingResult) {
	
		RequestInfo requestInfo = reissueCertRequest.getRequestInfo();
		ReissueCertAppl reissueApplication=reissueCertRequest.getReissueApplication();
		
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForCreate(requestInfo,
				bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;
		log.info("Request in controller::"+reissueCertRequest);
		ReissueCertResponse reIssueCertAppResponse=marriageCertService.updateAsync(reissueCertRequest);
		
		return new ResponseEntity<>(reIssueCertAppResponse,HttpStatus.CREATED);
	}
 
}
