package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @JsonProperty("isValidationSuccessful")
    private Boolean validationSuccessful;
    @JsonProperty("UUID")
    private String uuid;
    private String tenantId;
    private String identity;

}
