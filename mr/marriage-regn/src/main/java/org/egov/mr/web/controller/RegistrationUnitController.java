package org.egov.mr.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.service.RegistrationUnitService;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.egov.mr.web.contract.RegnUnitRequest;
import org.egov.mr.web.contract.RegnUnitResponse;
import org.egov.mr.web.contract.RequestInfo;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.contract.ResponseInfo;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regnUnits")
public class RegistrationUnitController {

	public static final Logger LOGGER = LoggerFactory.getLogger(RegistrationUnitController.class);

	@Autowired
	private RegistrationUnitService registrationUnitService;

	@Autowired
	private ErrorHandler errorHandler;

	@PostMapping
	@RequestMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid RegnUnitRequest regnUnitRequest,
			BindingResult regnUnitRequestBindingResults) {

		LOGGER.info("regnUnitRequest : " + regnUnitRequest);

		RequestInfo requestInfo = regnUnitRequest.getRequestInfo();

		// Validation
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrors(requestInfo,
				regnUnitRequestBindingResults, null);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		List<RegistrationUnit> registrationUnitsList = new ArrayList<>();

		try {
			registrationUnitsList = registrationUnitService.createAsync(regnUnitRequest);
		} catch (Exception e) {
			LOGGER.error(" Error While Procssing the Request!");
			return errorHandler.getUnExpectedErrorResponse(regnUnitRequestBindingResults, requestInfo,
					"Encountered : " + e.getMessage());
		}

		return getSuccessResponse(registrationUnitsList, requestInfo);
	}

	@PostMapping
	@RequestMapping("_search")
	public ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult bindingResultsForRequestInfoWrapper,
			@ModelAttribute @Valid RegistrationUnitSearchCriteria regnUnitSearchCriteria,
			BindingResult bindingResultsForSearchCriteria) {

		LOGGER.info("requestInfoWrapper : " + requestInfoWrapper);
		LOGGER.info("regnUnitSearchCriteria : " + regnUnitSearchCriteria);

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		// Validation
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrors(requestInfo,
				bindingResultsForRequestInfoWrapper, bindingResultsForSearchCriteria);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		List<RegistrationUnit> registrationUnitsList = new ArrayList<>();

		try {
			registrationUnitsList = registrationUnitService.search(regnUnitSearchCriteria);
		} catch (Exception e) {
			LOGGER.error(" Error While Procssing the Request!");
			return errorHandler.getUnExpectedErrorResponse(bindingResultsForSearchCriteria, requestInfo,
					"Encountered : " + e.getMessage());
		}

		return getSuccessResponse(registrationUnitsList, requestInfo);
	}

	@PostMapping
	@RequestMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid RegnUnitRequest regnUnitRequest,
			BindingResult regnUnitRequestBindingResults) {

		LOGGER.info("regnUnitRequest : " + regnUnitRequest);

		RequestInfo requestInfo = regnUnitRequest.getRequestInfo();

		// Validation
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrors(requestInfo,
				regnUnitRequestBindingResults, null);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		// KafKa
		List<RegistrationUnit> registrationUnitsList = new ArrayList<>();
		try {
			registrationUnitsList = registrationUnitService.updateAsync(regnUnitRequest);
			if (registrationUnitsList.isEmpty()) {
				return errorHandler.getUpdateErrorResponse(regnUnitRequestBindingResults, requestInfo,
						regnUnitRequest.getRegnUnit());
			}
		} catch (Exception e) {
			LOGGER.error(" Error While Procssing the Request!");
			return errorHandler.getUnExpectedErrorResponse(regnUnitRequestBindingResults, requestInfo,
					"Encountered : " + e.getCause());
		}

		return getSuccessResponse(registrationUnitsList, requestInfo);
	}

	// Returning the Response to method in the same class
	private ResponseEntity<?> getSuccessResponse(List<RegistrationUnit> registrationUnitsList,
			RequestInfo requestInfo) {

		LOGGER.info("regnUnitResponse : " + registrationUnitsList);

		RegnUnitResponse regnUnitResponse = new RegnUnitResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		// Setting ResponseInfo
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setKey(requestInfo.getKey());
		responseInfo.setResMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.OK.toString());
		responseInfo.setTenantId(requestInfo.getTenantId());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());
		// Setting regnUnitResponse responseInfo
		regnUnitResponse.setResponseInfo(responseInfo);
		// Setting regnUnitResponse registrationUnitsList
		regnUnitResponse.setRegnUnits(registrationUnitsList);

		LOGGER.info("regnUnitResponse : " + regnUnitResponse);

		return new ResponseEntity<RegnUnitResponse>(regnUnitResponse, HttpStatus.OK);
	}
}
