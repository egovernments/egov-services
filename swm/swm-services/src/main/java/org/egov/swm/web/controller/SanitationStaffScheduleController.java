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

        sanitationStaffScheduleRequest = sanitationStaffScheduleService.create(sanitationStaffScheduleRequest);

        return SanitationStaffScheduleResponse.builder()
                .responseInfo(getResponseInfo(sanitationStaffScheduleRequest.getRequestInfo()))
                .sanitationStaffSchedules(sanitationStaffScheduleRequest.getSanitationStaffSchedules()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public SanitationStaffScheduleResponse update(
            @RequestBody @Valid SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        sanitationStaffScheduleRequest = sanitationStaffScheduleService.update(sanitationStaffScheduleRequest);

        return SanitationStaffScheduleResponse.builder()
                .responseInfo(getResponseInfo(sanitationStaffScheduleRequest.getRequestInfo()))
                .sanitationStaffSchedules(sanitationStaffScheduleRequest.getSanitationStaffSchedules()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public SanitationStaffScheduleResponse search(
            @ModelAttribute final SanitationStaffScheduleSearch sanitationStaffScheduleSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<SanitationStaffSchedule> sanitationStaffScheduleList = sanitationStaffScheduleService
                .search(sanitationStaffScheduleSearch);

        return SanitationStaffScheduleResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .sanitationStaffSchedules(sanitationStaffScheduleList.getPagedData())
                .page(new PaginationContract(sanitationStaffScheduleList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}