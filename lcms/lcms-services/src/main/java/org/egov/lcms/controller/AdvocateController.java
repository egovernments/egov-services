package org.egov.lcms.controller;

import javax.validation.Valid;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocateRequest;
import org.egov.lcms.models.AdvocateResponse;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.service.AdvocateService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/legalcase/advocate/")
public class AdvocateController {

	@Autowired
	AdvocateService advocateService;

	@Autowired
	ResponseFactory responseInfoFactory;
	
	@Autowired
	PropertiesManager propertiesManager;

	@RequestMapping(path = "_create")
	public ResponseEntity<?> createAdvocate(@RequestBody @Valid AdvocateRequest advocateRequest) throws Exception {

		AdvocateResponse advocateResponse = advocateService.createAdvocate(advocateRequest);
		return new ResponseEntity<>(advocateResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_update")
	public ResponseEntity<?> updateAdvocate(@RequestBody @Valid AdvocateRequest advocateRequest) throws Exception {

		AdvocateResponse advocateResponse = advocateService.updateAdvocate(advocateRequest);
		return new ResponseEntity<>(advocateResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_search")
	public ResponseEntity<?> searchAdvocate(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid AdvocateSearchCriteria advocateSearchCriteria, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new CustomException(propertiesManager.getInvalidTenantCode(), propertiesManager.getExceptionMessage());
		}
		AdvocateResponse advocateResponse = advocateService.searchAdvocate(advocateSearchCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(advocateResponse, HttpStatus.CREATED);
	}
}