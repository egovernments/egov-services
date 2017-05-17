package org.egov.workflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ComplaintStatus {
    private Long id;
    private String name;
    private String tenantId;
    private String code;
}
