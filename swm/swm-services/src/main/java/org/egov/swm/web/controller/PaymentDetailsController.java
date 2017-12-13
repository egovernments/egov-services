package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.PaymentDetails;
import org.egov.swm.domain.model.PaymentDetailsSearch;
import org.egov.swm.domain.service.PaymentDetailsService;
import org.egov.swm.web.requests.PaymentDetailsRequest;
import org.egov.swm.web.requests.PaymentDetailsResponse;
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
@RequestMapping("/paymentdetails")
public class PaymentDetailsController {

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDetailsResponse create(
            @RequestBody @Valid PaymentDetailsRequest paymentDetailsRequest) {

        paymentDetailsRequest = paymentDetailsService.create(paymentDetailsRequest);

        return PaymentDetailsResponse.builder()
                .responseInfo(getResponseInfo(paymentDetailsRequest.getRequestInfo()))
                .paymentDetails(paymentDetailsRequest.getPaymentDetails()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDetailsResponse update(
            @RequestBody @Valid PaymentDetailsRequest paymentDetailsRequest) {

        paymentDetailsRequest = paymentDetailsService.update(paymentDetailsRequest);

        return PaymentDetailsResponse.builder()
                .responseInfo(getResponseInfo(paymentDetailsRequest.getRequestInfo()))
                .paymentDetails(paymentDetailsRequest.getPaymentDetails()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PaymentDetailsResponse search(@ModelAttribute final PaymentDetailsSearch paymentDetailsSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<PaymentDetails> paymentDetailsList = paymentDetailsService
                .search(paymentDetailsSearch);

        return PaymentDetailsResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .paymentDetails(paymentDetailsList.getPagedData())
                .page(new PaginationContract(paymentDetailsList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}
