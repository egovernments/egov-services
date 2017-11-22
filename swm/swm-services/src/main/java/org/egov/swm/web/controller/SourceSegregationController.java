package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.SourceSegregation;
import org.egov.swm.domain.model.SourceSegregationSearch;
import org.egov.swm.domain.service.SourceSegregationService;
import org.egov.swm.web.requests.SourceSegregationRequest;
import org.egov.swm.web.requests.SourceSegregationResponse;
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
@RequestMapping("/sourcesegregations")
public class SourceSegregationController {

    @Autowired
    private SourceSegregationService sourceSegregationService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public SourceSegregationResponse create(@RequestBody @Valid SourceSegregationRequest sourceSegregationRequest) {

        sourceSegregationRequest = sourceSegregationService.create(sourceSegregationRequest);

        return SourceSegregationResponse.builder()
                .responseInfo(getResponseInfo(sourceSegregationRequest.getRequestInfo()))
                .sourceSegregations(sourceSegregationRequest.getSourceSegregations()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public SourceSegregationResponse update(@RequestBody @Valid SourceSegregationRequest sourceSegregationRequest) {

        sourceSegregationRequest = sourceSegregationService.update(sourceSegregationRequest);

        return SourceSegregationResponse.builder()
                .responseInfo(getResponseInfo(sourceSegregationRequest.getRequestInfo()))
                .sourceSegregations(sourceSegregationRequest.getSourceSegregations()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public SourceSegregationResponse search(@ModelAttribute final SourceSegregationSearch sourceSegregationSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<SourceSegregation> sourceSegregationList = sourceSegregationService.search(sourceSegregationSearch);

        return SourceSegregationResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .sourceSegregations(sourceSegregationList.getPagedData())
                .page(new PaginationContract(sourceSegregationList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}