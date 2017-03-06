package org.egov.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OtpValidationResponse {
    private ResponseInfo responseInfo;
    @JsonProperty("isSuccessful")
    private boolean successful;

    public OtpValidationResponse(boolean isSuccessful) {
        this.successful = isSuccessful;
    }
}
