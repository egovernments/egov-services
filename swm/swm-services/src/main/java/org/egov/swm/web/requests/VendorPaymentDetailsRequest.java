package org.egov.swm.web.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.VendorPaymentDetails;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public @Data  class VendorPaymentDetailsRequest {

    @Valid
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = new RequestInfo();
    @Valid
    private List<VendorPaymentDetails> vendorPaymentDetails = new ArrayList<VendorPaymentDetails>();
}
