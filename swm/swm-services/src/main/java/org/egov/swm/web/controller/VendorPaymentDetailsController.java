package org.egov.swm.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.service.VendorPaymentDetailsService;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.egov.swm.web.requests.VendorPaymentDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vendorpaymentdetails")
public class VendorPaymentDetailsController {

    private VendorPaymentDetailsService vendorPaymentDetailsService;

    public VendorPaymentDetailsController(VendorPaymentDetailsService vendorPaymentDetailsService) {
        this.vendorPaymentDetailsService = vendorPaymentDetailsService;
    }

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorPaymentDetailsResponse create(@RequestBody @Valid VendorPaymentDetailsRequest vendorPaymentDetailsRequest){

        VendorPaymentDetailsResponse vendorPaymentDetailsResponse = new VendorPaymentDetailsResponse();
        vendorPaymentDetailsResponse.setResponseInfo(getResponseInfo(vendorPaymentDetailsRequest.getRequestInfo()));

        vendorPaymentDetailsRequest = vendorPaymentDetailsService.create(vendorPaymentDetailsRequest);

        vendorPaymentDetailsResponse.setVendorPaymentDetails(vendorPaymentDetailsRequest.getVendorPaymentDetails());

        return vendorPaymentDetailsResponse;
    }


    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorPaymentDetailsResponse update(@RequestBody @Valid VendorPaymentDetailsRequest vendorPaymentDetailsRequest){

        VendorPaymentDetailsResponse vendorPaymentDetailsResponse = new VendorPaymentDetailsResponse();
        vendorPaymentDetailsResponse.setResponseInfo(getResponseInfo(vendorPaymentDetailsRequest.getRequestInfo()));

        vendorPaymentDetailsRequest = vendorPaymentDetailsService.create(vendorPaymentDetailsRequest);

        vendorPaymentDetailsResponse.setVendorPaymentDetails(vendorPaymentDetailsRequest.getVendorPaymentDetails());

        return vendorPaymentDetailsResponse;
    }


    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}
