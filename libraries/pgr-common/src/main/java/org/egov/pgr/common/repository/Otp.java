package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
class Otp {
    private String otp;
    private String identity;
    private String tenantId;
    @JsonProperty("isValidationSuccessful")
    private boolean validationSuccessful;
    @JsonProperty("UUID")
    private String uuid;

    public boolean isValidationComplete(String mobileNumber) {
        return validationSuccessful && identity.equals(mobileNumber);
    }
}

