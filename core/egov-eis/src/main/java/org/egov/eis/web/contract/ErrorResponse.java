package org.egov.eis.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("response_info")
    private ResponseInfo resposneInfo;

    @JsonProperty("errors")
    private List<Error> errors;

}

