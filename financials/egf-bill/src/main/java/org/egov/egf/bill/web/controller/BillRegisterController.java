package org.egov.egf.bill.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.egf.bill.constants.Constants;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.domain.service.BillRegisterService;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.bill.web.requests.BillRegisterResponse;
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
@RequestMapping("/bills")
public class BillRegisterController {

    @Autowired
    private BillRegisterService billRegisterService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public BillRegisterResponse create(@RequestBody BillRegisterRequest billRegisterRequest) {

        billRegisterRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);

        billRegisterRequest = billRegisterService.create(billRegisterRequest);

        return BillRegisterResponse.builder().responseInfo(getResponseInfo(billRegisterRequest.getRequestInfo()))
                .billRegisters(billRegisterRequest.getBillRegisters()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public BillRegisterResponse update(@RequestBody BillRegisterRequest billRegisterRequest) {

        billRegisterRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);

        billRegisterRequest = billRegisterService.update(billRegisterRequest);

        return BillRegisterResponse.builder().responseInfo(getResponseInfo(billRegisterRequest.getRequestInfo()))
                .billRegisters(billRegisterRequest.getBillRegisters()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BillRegisterResponse search(@ModelAttribute final BillRegisterSearch billRegisterSearch,
            final RequestInfo requestInfo, @RequestParam final String tenantId) {

        billRegisterSearch.setTenantId(tenantId);

        final Pagination<BillRegister> billRegisterList = billRegisterService.search(billRegisterSearch);

        return BillRegisterResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .billRegisters(billRegisterList.getPagedData()).page(new Pagination(billRegisterList))
                .build();
    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer()).resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder").status("placeholder").build();
    }
}