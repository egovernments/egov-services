package org.egov.pgr.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.pgr.model.Error;
import org.egov.pgr.model.ResponseInfo;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("response_info")
    private ResponseInfo resposneInfo;

    @JsonProperty("error")
    private Error error;

}

