package org.egov.collection.web.controller;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.collection.model.RequestInfoWrapper;
import org.egov.collection.service.CollectionConfigService;
import org.egov.collection.web.contract.CollectionConfigGetRequest;
import org.egov.collection.web.contract.CollectionConfigResponse;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.errorhandlers.ErrorHandler;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collectionconfig")
public class CollectionConfigController {

	private static final Logger logger = LoggerFactory.getLogger(CollectionConfigController.class);

	@Autowired
	private CollectionConfigService collectionConfigService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid CollectionConfigGetRequest collectionConfigGetRequest,
			BindingResult bindingResult, @RequestBody RequestInfoWrapper requestInfoWrapper) {
		
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		// validate header
		if(requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null ) {
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}
		// validate input params
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo);
		}
		// Call service
		Map<String, List<String>> collectionConfigKeyValMap = null;
		try {
			collectionConfigKeyValMap = collectionConfigService.getCollectionConfiguration(collectionConfigGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + collectionConfigGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(collectionConfigKeyValMap, requestInfo);
	}

	/**
	 * Populate Response object and return collectionConfigKeyValMap
	 * 
	 * @param collectionConfigKeyValMap
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(Map<String, List<String>> collectionConfigKeyValMap, RequestInfo requestInfo) {
		CollectionConfigResponse collectionConfigResponse = new CollectionConfigResponse();
		collectionConfigResponse.setCollectionConfiguration(collectionConfigKeyValMap);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		collectionConfigResponse.setResponseInfo(responseInfo);

		return new ResponseEntity<>(collectionConfigResponse, HttpStatus.OK);

	}
}
