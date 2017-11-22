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

        sanitationStaffTargetRequest = sanitationStaffTargetService.create(sanitationStaffTargetRequest);

        return SanitationStaffTargetResponse.builder()
                .responseInfo(getResponseInfo(sanitationStaffTargetRequest.getRequestInfo()))
                .sanitationStaffTargets(sanitationStaffTargetRequest.getSanitationStaffTargets()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public SanitationStaffTargetResponse update(
            @RequestBody @Valid SanitationStaffTargetRequest sanitationStaffTargetRequest) {

        sanitationStaffTargetRequest = sanitationStaffTargetService.update(sanitationStaffTargetRequest);

        return SanitationStaffTargetResponse.builder()
                .responseInfo(getResponseInfo(sanitationStaffTargetRequest.getRequestInfo()))
                .sanitationStaffTargets(sanitationStaffTargetRequest.getSanitationStaffTargets()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public SanitationStaffTargetResponse search(@ModelAttribute final SanitationStaffTargetSearch sanitationStaffTargetSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<SanitationStaffTarget> sanitationStaffTargetList = sanitationStaffTargetService
                .search(sanitationStaffTargetSearch);

        return SanitationStaffTargetResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .sanitationStaffTargets(sanitationStaffTargetList.getPagedData())
                .page(new PaginationContract(sanitationStaffTargetList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}