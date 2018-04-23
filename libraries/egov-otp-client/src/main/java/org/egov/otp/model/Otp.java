package org.egov.otp.model;

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
public class Otp {
    private String otp;
    @JsonProperty("UUID")
    private String uuid;
    private String identity;
    private String tenantId;
    @JsonProperty("isValidationSuccessful")
    private boolean validationSuccessful;
}
