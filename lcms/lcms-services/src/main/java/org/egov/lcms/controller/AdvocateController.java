package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocateResponse;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.AgencyRequest;
import org.egov.lcms.models.AgencyResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(path = "_search")
	public ResponseEntity<?> searchAdvocate(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid AdvocateSearchCriteria advocateSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new CustomException(propertiesManager.getInvalidTenantCode(),
					propertiesManager.getExceptionMessage());
		}
		AdvocateResponse advocateResponse = advocateService.searchAdvocate(advocateSearchCriteria,
				requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(advocateResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_create")
	public ResponseEntity<?> createAgency(@RequestBody @Valid AgencyRequest agencyRequest) throws Exception {

		AgencyResponse agencyResponse = advocateService.createAgency(agencyRequest);
		return new ResponseEntity<>(agencyResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_update")
	public ResponseEntity<?> updateAdvocate(@RequestBody @Valid AgencyRequest agencyRequest) throws Exception {

		AgencyResponse advocateResponse = advocateService.updateAgency(agencyRequest);
		return new ResponseEntity<>(advocateResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "agency/_search")
	public ResponseEntity<?> searchAgency(@RequestParam(name = "tenantId", required = true) String tenantId,
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "isIndividual", required = false) Boolean isIndividual,
			@RequestParam(name = "advocateName", required = false) String advocateName,
			@RequestParam(name = "agencyName", required = false) String agencyName,
			@RequestBody @Valid RequestInfoWrapper requestInfoWrapper) throws Exception {

		AgencyResponse agencyResponse = advocateService.searchAgency(tenantId, code, isIndividual, advocateName, agencyName,
				requestInfoWrapper);
		return new ResponseEntity<>(agencyResponse, HttpStatus.CREATED);
	}

}