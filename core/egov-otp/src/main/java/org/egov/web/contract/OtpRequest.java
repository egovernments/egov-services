package org.egov.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpRequest {
    private RequestInfo requestInfo;
    private Otp otp;

    @JsonIgnore
    public String getIdentity() {
        return otp != null ? otp.getIdentity() : null;
    }

    @JsonIgnore
    public String getTenantId() {
        return otp != null ? otp.getTenantId() : null;
    }
}

