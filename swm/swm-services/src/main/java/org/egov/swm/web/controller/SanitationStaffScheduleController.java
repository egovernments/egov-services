package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.SanitationStaffSchedule;
import org.egov.swm.domain.model.SanitationStaffScheduleSearch;
import org.egov.swm.domain.service.SanitationStaffScheduleService;
import org.egov.swm.web.requests.SanitationStaffScheduleRequest;
import org.egov.swm.web.requests.SanitationStaffScheduleResponse;
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
@RequestMapping("/sanitationstaffschedules")
public class SanitationStaffScheduleController {

	@Autowired
	private SanitationStaffScheduleService sanitationStaffScheduleService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public SanitationStaffScheduleResponse create(
			@RequestBody @Valid SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

		SanitationStaffScheduleResponse sanitationStaffScheduleResponse = new SanitationStaffScheduleResponse();
		sanitationStaffScheduleResponse
				.setResponseInfo(getResponseInfo(sanitationStaffScheduleRequest.getRequestInfo()));

		sanitationStaffScheduleRequest = sanitationStaffScheduleService.create(sanitationStaffScheduleRequest);

		sanitationStaffScheduleResponse
				.setSanitationStaffSchedules(sanitationStaffScheduleRequest.getSanitationStaffSchedules());

		return sanitationStaffScheduleResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public SanitationStaffScheduleResponse update(
			@RequestBody @Valid SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

		SanitationStaffScheduleResponse sanitationStaffScheduleResponse = new SanitationStaffScheduleResponse();
		sanitationStaffScheduleResponse
				.setResponseInfo(getResponseInfo(sanitationStaffScheduleRequest.getRequestInfo()));

		sanitationStaffScheduleRequest = sanitationStaffScheduleService.update(sanitationStaffScheduleRequest);

		sanitationStaffScheduleResponse
				.setSanitationStaffSchedules(sanitationStaffScheduleRequest.getSanitationStaffSchedules());

		return sanitationStaffScheduleResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SanitationStaffScheduleResponse search(
			@ModelAttribute SanitationStaffScheduleSearch sanitationStaffScheduleSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<SanitationStaffSchedule> sanitationStaffScheduleList = sanitationStaffScheduleService
				.search(sanitationStaffScheduleSearch);

		SanitationStaffScheduleResponse response = new SanitationStaffScheduleResponse();
		response.setSanitationStaffSchedules(sanitationStaffScheduleList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(sanitationStaffScheduleList));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}