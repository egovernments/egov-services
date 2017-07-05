package org.egov.lams.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestInfoBody {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

}