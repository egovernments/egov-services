package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.domain.model.ShiftSearch;
import org.egov.swm.domain.service.ShiftService;
import org.egov.swm.web.requests.ShiftRequest;
import org.egov.swm.web.requests.ShiftResponse;
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
@RequestMapping("/shifts")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftResponse create(@RequestBody @Valid ShiftRequest shiftRequest) {

        shiftRequest = shiftService.create(shiftRequest);

        return ShiftResponse.builder().responseInfo(getResponseInfo(shiftRequest.getRequestInfo()))
                .shifts(shiftRequest.getShifts()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftResponse update(@RequestBody @Valid ShiftRequest shiftRequest) {

        shiftRequest = shiftService.update(shiftRequest);

        return ShiftResponse.builder().responseInfo(getResponseInfo(shiftRequest.getRequestInfo()))
                .shifts(shiftRequest.getShifts()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ShiftResponse search(@ModelAttribute final ShiftSearch shiftSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<Shift> shiftList = shiftService.search(shiftSearch);

        return ShiftResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .shifts(shiftList.getPagedData()).page(new PaginationContract(shiftList))
                .build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}