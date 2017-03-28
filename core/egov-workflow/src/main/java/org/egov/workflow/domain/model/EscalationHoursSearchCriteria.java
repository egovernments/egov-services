package org.egov.workflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class EscalationHoursSearchCriteria {
    private String tenantId;
    private Long designationId;
    private Long complaintTypeId;
}
