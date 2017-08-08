package org.egov.pgr.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@AllArgsConstructor
public class RequestInfoBody {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

}