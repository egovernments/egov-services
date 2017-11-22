package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.domain.service.VendorPaymentDetailsService;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.egov.swm.web.requests.VendorPaymentDetailsResponse;
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
@RequestMapping("/vendorpaymentdetails")
public class VendorPaymentDetailsController {

    private final VendorPaymentDetailsService vendorPaymentDetailsService;

    public VendorPaymentDetailsController(final VendorPaymentDetailsService vendorPaymentDetailsService) {
        this.vendorPaymentDetailsService = vendorPaymentDetailsService;
    }

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorPaymentDetailsResponse create(
            @RequestBody @Valid VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        vendorPaymentDetailsRequest = vendorPaymentDetailsService.create(vendorPaymentDetailsRequest);

        return VendorPaymentDetailsResponse.builder()
                .responseInfo(getResponseInfo(vendorPaymentDetailsRequest.getRequestInfo()))
                .vendorPaymentDetails(vendorPaymentDetailsRequest.getVendorPaymentDetails()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorPaymentDetailsResponse update(
            @RequestBody @Valid VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        vendorPaymentDetailsRequest = vendorPaymentDetailsService.update(vendorPaymentDetailsRequest);

        return VendorPaymentDetailsResponse.builder()
                .responseInfo(getResponseInfo(vendorPaymentDetailsRequest.getRequestInfo()))
                .vendorPaymentDetails(vendorPaymentDetailsRequest.getVendorPaymentDetails()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VendorPaymentDetailsResponse search(@ModelAttribute final VendorPaymentDetailsSearch vendorPaymentDetailsSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<VendorPaymentDetails> vendorPaymentDetailsList = vendorPaymentDetailsService
                .search(vendorPaymentDetailsSearch);

        return VendorPaymentDetailsResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .vendorPaymentDetails(vendorPaymentDetailsList.getPagedData())
                .page(new PaginationContract(vendorPaymentDetailsList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}
