package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkflowResponse {

    @JsonProperty("state_id")
    private String stateId;

    @JsonProperty("assignee")
    private String assignee;

}
