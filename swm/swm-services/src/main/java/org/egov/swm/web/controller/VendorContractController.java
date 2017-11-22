package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.service.VendorContractService;
import org.egov.swm.web.requests.VendorContractRequest;
import org.egov.swm.web.requests.VendorContractResponse;
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
@RequestMapping("/vendorcontracts")
public class VendorContractController {

    @Autowired
    private VendorContractService vendorContractService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorContractResponse create(@RequestBody @Valid VendorContractRequest vendorContractRequest) {

        vendorContractRequest = vendorContractService.create(vendorContractRequest);

        return VendorContractResponse.builder().responseInfo(getResponseInfo(vendorContractRequest.getRequestInfo()))
                .vendorContracts(vendorContractRequest.getVendorContracts()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorContractResponse update(@RequestBody @Valid VendorContractRequest vendorContractRequest) {

        vendorContractRequest = vendorContractService.update(vendorContractRequest);

        return VendorContractResponse.builder().responseInfo(getResponseInfo(vendorContractRequest.getRequestInfo()))
                .vendorContracts(vendorContractRequest.getVendorContracts()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VendorContractResponse search(@ModelAttribute final VendorContractSearch vendorContractSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<VendorContract> vendorContractList = vendorContractService.search(vendorContractSearch);

        return VendorContractResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .vendorContracts(vendorContractList.getPagedData()).page(new PaginationContract(vendorContractList))
                .build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}