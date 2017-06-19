package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
class RequestInfoWrapper {

    @JsonProperty("RequestInfo")
    private org.egov.common.contract.request.RequestInfo RequestInfo;
}
