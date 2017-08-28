package org.egov.mr.web.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.service.ServiceConfigurationService;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.contract.ServiceConfigResponse;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
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
@RequestMapping("/serviceConfigurations")
public class ServiceConfigurationController {
	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfigurationController.class);

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private ServiceConfigurationService serviceConfigurationService;

	@PostMapping
	@RequestMapping("/_search")
	public ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult bindingResultsForRequestInfoWrapper,
			@ModelAttribute @Valid ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria,
			BindingResult bindingResultForserviceConfigurationSearchCriteria) {

		LOGGER.info("requestInfoWrapper : " + requestInfoWrapper);
		LOGGER.info("serviceConfigurationSearchCriteria : " + serviceConfigurationSearchCriteria);

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		// Validation
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForSearch(requestInfo,
				bindingResultsForRequestInfoWrapper, bindingResultForserviceConfigurationSearchCriteria);

		if (errorResponseEntity != null)
			return errorResponseEntity;
		// Entering service method
		Map<String, List<String>> serviceConfigKeyValuesList = null;
		try {
			serviceConfigKeyValuesList = serviceConfigurationService.search(serviceConfigurationSearchCriteria);
		} catch (Exception e) {
			LOGGER.info(" Error While Procssing the Request!");
			return errorHandler.getUnExpectedErrorResponse(bindingResultsForRequestInfoWrapper, requestInfo,
					"Encountered : " + e.getMessage());
		}
		return getSuccessResponseForSearch(serviceConfigKeyValuesList, requestInfo);
	}

	// returning the Response to method in the same class
	private ResponseEntity<?> getSuccessResponseForSearch(Map<String, List<String>> serviceConfigKeyValuesList,
			RequestInfo requestInfo) {
		// Setting ResponseInfo
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		//responseInfo.setKey(requestInfo.getKey());
		responseInfo.setResMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.OK.toString());
		//responseInfo.setTenantId(requestInfo.getTenantId());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());
		ServiceConfigResponse serviceConfigResponse = new ServiceConfigResponse();
		// Setting regnUnitResponse responseInfo
		serviceConfigResponse.setResponseInfo(responseInfo);
		// Setting regnUnitResponse registrationUnitsList
		serviceConfigResponse.setServiceConfiguration(serviceConfigKeyValuesList);
		return new ResponseEntity<ServiceConfigResponse>(serviceConfigResponse, HttpStatus.OK);
	}
}
