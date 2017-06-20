package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@AllArgsConstructor
public class OtpRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    private String mobileNumber;
    private String tenantId;

    public org.egov.pgr.common.model.OtpRequest toDomain() {
        return org.egov.pgr.common.model.OtpRequest.builder()
                .mobileNumber(mobileNumber)
                .tenantId(tenantId)
                .build();
    }
}

