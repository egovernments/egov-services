package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Builder
@Getter
@AllArgsConstructor
public class EscalationHoursRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}

