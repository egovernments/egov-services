package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OtpRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    private Otp otp;
}
