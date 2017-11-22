package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.VendorService;
import org.egov.swm.web.requests.VendorRequest;
import org.egov.swm.web.requests.VendorResponse;
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
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorResponse create(@RequestBody @Valid VendorRequest vendorRequest) {

        vendorRequest = vendorService.create(vendorRequest);

        return VendorResponse.builder().responseInfo(getResponseInfo(vendorRequest.getRequestInfo()))
                .vendors(vendorRequest.getVendors()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorResponse update(@RequestBody @Valid VendorRequest vendorRequest) {

        vendorRequest = vendorService.update(vendorRequest);

        return VendorResponse.builder().responseInfo(getResponseInfo(vendorRequest.getRequestInfo()))
                .vendors(vendorRequest.getVendors()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VendorResponse search(@ModelAttribute final VendorSearch vendorSearch, @RequestBody final RequestInfo requestInfo,
            @RequestParam final String tenantId) {

        final Pagination<Vendor> vendorList = vendorService.search(vendorSearch);

        return VendorResponse.builder().responseInfo(getResponseInfo(requestInfo)).vendors(vendorList.getPagedData())
                .page(new PaginationContract(vendorList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}