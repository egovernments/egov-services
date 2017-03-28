package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EscalationHoursRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}

