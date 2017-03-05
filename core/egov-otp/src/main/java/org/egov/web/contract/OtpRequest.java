package org.egov.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.model.TokenRequest;

@Getter
@AllArgsConstructor
public class OtpRequest {
    private RequestInfo requestInfo;
    private Otp otp;

    public TokenRequest getTokenRequest() {
        return new TokenRequest(getIdentity(), getTenantId());
    }

    @JsonIgnore
    private String getIdentity() {
        return otp != null ? otp.getIdentity() : null;
    }

    @JsonIgnore
    private String getTenantId() {
        return otp != null ? otp.getTenantId() : null;
    }
}

