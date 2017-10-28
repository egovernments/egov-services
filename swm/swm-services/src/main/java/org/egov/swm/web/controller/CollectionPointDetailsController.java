package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.service.CollectionPointDetailsService;
import org.egov.swm.web.requests.CollectionPointDetailsRequest;
import org.egov.swm.web.requests.CollectionPointDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collectionpointdetails")
public class CollectionPointDetailsController {

	@Autowired
	private CollectionPointDetailsService collectionPointDetailsService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CollectionPointDetailsResponse create(
			@RequestBody @Valid CollectionPointDetailsRequest collectionPointDetailsRequest) {

		CollectionPointDetailsResponse collectionPointDetailsResponse = new CollectionPointDetailsResponse();
		collectionPointDetailsResponse.setResponseInfo(getResponseInfo(collectionPointDetailsRequest.getRequestInfo()));

		collectionPointDetailsRequest = collectionPointDetailsService.create(collectionPointDetailsRequest);

		collectionPointDetailsResponse
				.setCollectionPointDetails(collectionPointDetailsRequest.getCollectionPointDetails());

		return collectionPointDetailsResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CollectionPointDetailsResponse update(
			@RequestBody @Valid CollectionPointDetailsRequest collectionPointDetailsRequest) {

		CollectionPointDetailsResponse collectionPointDetailsResponse = new CollectionPointDetailsResponse();
		collectionPointDetailsResponse.setResponseInfo(getResponseInfo(collectionPointDetailsRequest.getRequestInfo()));

		collectionPointDetailsRequest = collectionPointDetailsService.update(collectionPointDetailsRequest);

		collectionPointDetailsResponse
				.setCollectionPointDetails(collectionPointDetailsRequest.getCollectionPointDetails());

		return collectionPointDetailsResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CollectionPointDetailsResponse search(
			@ModelAttribute CollectionPointDetailsSearch collectionPointDetailsSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<CollectionPointDetails> collectionPointDetailsList = collectionPointDetailsService
				.search(collectionPointDetailsSearch);

		CollectionPointDetailsResponse response = new CollectionPointDetailsResponse();
		response.setCollectionPointDetails(collectionPointDetailsList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}