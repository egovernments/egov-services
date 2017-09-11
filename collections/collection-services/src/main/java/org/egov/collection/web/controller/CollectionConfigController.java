package org.egov.collection.web.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.collection.model.RequestInfoWrapper;
import org.egov.collection.service.CollectionConfigService;
import org.egov.collection.web.contract.CollectionConfigGetRequest;
import org.egov.collection.web.contract.CollectionConfigResponse;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
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

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CollectionConfigController.class);

	@Autowired
	private CollectionConfigService collectionConfigService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(
			@ModelAttribute @Valid CollectionConfigGetRequest collectionConfigGetRequest,
			BindingResult bindingResult,
			@RequestBody RequestInfoWrapper requestInfoWrapper) {

		// Call service
		Map<String, List<String>> collectionConfigKeyValMap = collectionConfigService
				.getCollectionConfiguration(collectionConfigGetRequest);
		LOGGER.info("Search Config Map::::::" + collectionConfigKeyValMap);
		return getSuccessResponse(collectionConfigKeyValMap,
				requestInfoWrapper.getRequestInfo());
	}

	/**
	 * Populate Response object and return collectionConfigKeyValMap
	 * 
	 * @param collectionConfigKeyValMap
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(
			Map<String, List<String>> collectionConfigKeyValMap,
			RequestInfo requestInfo) {
		CollectionConfigResponse collectionConfigResponse = new CollectionConfigResponse();
		collectionConfigResponse
				.setCollectionConfiguration(collectionConfigKeyValMap);
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		collectionConfigResponse.setResponseInfo(responseInfo);

		return new ResponseEntity<>(collectionConfigResponse, HttpStatus.OK);

	}
}
