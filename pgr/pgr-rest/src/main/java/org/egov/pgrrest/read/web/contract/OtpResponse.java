package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@AllArgsConstructor
public class OtpResponse {
    private ResponseInfo responseInfo;
    @JsonProperty("isSuccessful")
    private boolean successful;
}


