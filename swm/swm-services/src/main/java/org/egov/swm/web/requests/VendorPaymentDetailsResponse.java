package org.egov.swm.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VendorPaymentDetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data class VendorPaymentDetailsResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    private List<VendorPaymentDetails> vendorPaymentDetails;
    private PaginationContract page;
}
