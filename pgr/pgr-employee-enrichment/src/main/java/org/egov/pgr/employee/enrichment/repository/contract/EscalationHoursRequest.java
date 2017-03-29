package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;

@Builder
@Getter
@AllArgsConstructor
public class EscalationHoursRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}

