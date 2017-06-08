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
public class WorkflowDetails {

    @NotNull
    private long department;

    @NotNull
    private long designation;

    @NotNull
    private long approver;

    @NotNull
    private String comments;

    private Long initiatorPosition;
    private Long assignee;
    private String action;

    @NotNull
    private String status;

}
