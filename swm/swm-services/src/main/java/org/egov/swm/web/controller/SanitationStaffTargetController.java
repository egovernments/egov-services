package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.service.SanitationStaffTargetService;
import org.egov.swm.web.requests.SanitationStaffTargetRequest;
import org.egov.swm.web.requests.SanitationStaffTargetResponse;
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
@RequestMapping("/sanitationstafftargets")
public class SanitationStaffTargetController {

	@Autowired
	private SanitationStaffTargetService sanitationStaffTargetService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public SanitationStaffTargetResponse create(
			@RequestBody @Valid SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		SanitationStaffTargetResponse sanitationStaffTargetResponse = new SanitationStaffTargetResponse();
		sanitationStaffTargetResponse.setResponseInfo(getResponseInfo(sanitationStaffTargetRequest.getRequestInfo()));

		sanitationStaffTargetRequest = sanitationStaffTargetService.create(sanitationStaffTargetRequest);

		sanitationStaffTargetResponse
				.setSanitationStaffTargets(sanitationStaffTargetRequest.getSanitationStaffTargets());

		return sanitationStaffTargetResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public SanitationStaffTargetResponse update(
			@RequestBody @Valid SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		SanitationStaffTargetResponse sanitationStaffTargetResponse = new SanitationStaffTargetResponse();
		sanitationStaffTargetResponse.setResponseInfo(getResponseInfo(sanitationStaffTargetRequest.getRequestInfo()));

		sanitationStaffTargetRequest = sanitationStaffTargetService.update(sanitationStaffTargetRequest);

		sanitationStaffTargetResponse
				.setSanitationStaffTargets(sanitationStaffTargetRequest.getSanitationStaffTargets());

		return sanitationStaffTargetResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SanitationStaffTargetResponse search(@ModelAttribute SanitationStaffTargetSearch sanitationStaffTargetSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<SanitationStaffTarget> sanitationStaffTargetList = sanitationStaffTargetService
				.search(sanitationStaffTargetSearch);

		SanitationStaffTargetResponse response = new SanitationStaffTargetResponse();
		response.setSanitationStaffTargets(sanitationStaffTargetList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(sanitationStaffTargetList));
		
		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}