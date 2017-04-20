package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@AllArgsConstructor
public class EscalationHoursResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    private int hours;
    private String tenantId;
}
