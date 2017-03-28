package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class EscalationHoursResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    private int hours;

    public EscalationHoursResponse(int escalationHours) {
        this.hours = escalationHours;
    }
}
