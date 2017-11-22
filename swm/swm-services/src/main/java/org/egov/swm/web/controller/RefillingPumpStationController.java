package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.service.RefillingPumpStationService;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.swm.web.requests.RefillingPumpStationResponse;
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
@RequestMapping("/refillingpumpstations")
public class RefillingPumpStationController {

    private final RefillingPumpStationService refillingPumpStationService;

    public RefillingPumpStationController(final RefillingPumpStationService refillingPumpStationService) {
        this.refillingPumpStationService = refillingPumpStationService;
    }

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public RefillingPumpStationResponse create(
            @RequestBody @Valid RefillingPumpStationRequest refillingPumpStationRequest) {

        refillingPumpStationRequest = refillingPumpStationService.create(refillingPumpStationRequest);

        return RefillingPumpStationResponse.builder()
                .responseInfo(getResponseInfo(refillingPumpStationRequest.getRequestInfo()))
                .refillingPumpStations(refillingPumpStationRequest.getRefillingPumpStations()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public RefillingPumpStationResponse update(
            @RequestBody @Valid RefillingPumpStationRequest refillingPumpStationRequest) {

        refillingPumpStationRequest = refillingPumpStationService.update(refillingPumpStationRequest);

        return RefillingPumpStationResponse.builder()
                .responseInfo(getResponseInfo(refillingPumpStationRequest.getRequestInfo()))
                .refillingPumpStations(refillingPumpStationRequest.getRefillingPumpStations()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RefillingPumpStationResponse search(@ModelAttribute final RefillingPumpStationSearch refillingPumpStationSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<RefillingPumpStation> refillingPumpStationList = refillingPumpStationService
                .search(refillingPumpStationSearch);

        return RefillingPumpStationResponse.builder().refillingPumpStations(refillingPumpStationList.getPagedData())
                .responseInfo(getResponseInfo(requestInfo)).page(new PaginationContract(refillingPumpStationList))
                .build();
    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }
}
