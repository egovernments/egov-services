package org.egov.otp.model;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpValidationResponse {
    private ResponseInfo responseInfo;
    @JsonProperty("isSuccessful")
    private boolean successful;

    public OtpValidationResponse(boolean isSuccessful) {
        this.successful = isSuccessful;
    }
}
