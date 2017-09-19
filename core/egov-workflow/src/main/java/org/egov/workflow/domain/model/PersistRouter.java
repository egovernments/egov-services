package org.egov.workflow.domain.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class PersistRouter {
    public static final String SEQ_ROUTER = "SEQ_EGPGR_ROUTER";

    @NotNull
    private Long id;

    private Long service;

    private Long boundary;

    private Boolean active;

    @NotNull
    private Integer position;

    @NotNull
    private String tenantId;

    private AuditDetails auditDetails;

}

