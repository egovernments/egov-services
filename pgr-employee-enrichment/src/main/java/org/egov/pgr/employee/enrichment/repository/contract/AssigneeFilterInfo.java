package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssigneeFilterInfo {

    @JsonProperty("boundary_id")
    private Long boundaryId;

    @JsonProperty("complaint_type_code")
    private String complaintTypeCode;

    @JsonProperty("current_assignee_id")
    private Long currentAssigneeId;

    public AssigneeFilterInfo(Long boundaryId, String complaintTypeCode) {
        this.boundaryId = boundaryId;
        this.complaintTypeCode = complaintTypeCode;
    }

}
