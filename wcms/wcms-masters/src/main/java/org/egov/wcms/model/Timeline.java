package org.egov.wcms.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class Timeline {

    @NotNull
    private long id;

    @NotNull
    private long connectionId;

    @NotNull
    private String remarks;

    @NotNull
    private User assigner;

    @NotNull
    private User asignee;

    @NotNull
    private WorkflowDetails workflowDetails;

    @NotNull
    private AuditDetails auditDetails;

    @NotNull
    private String tenantId;

}
