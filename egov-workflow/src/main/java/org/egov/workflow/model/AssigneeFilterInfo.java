package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AssigneeFilterInfo {

    @JsonProperty("boundary_id")
    private Long boundaryId;

    @JsonProperty("complaint_type_code")
    private String complaintTypeCode;

    @JsonProperty("current_assignee_id")
    private Long currentAssigneeId;

}
