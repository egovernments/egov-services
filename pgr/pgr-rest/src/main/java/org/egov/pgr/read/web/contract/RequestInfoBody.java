package org.egov.pgr.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
public class RequestInfoBody {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

}