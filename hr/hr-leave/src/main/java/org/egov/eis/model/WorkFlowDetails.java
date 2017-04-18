package org.egov.eis.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkFlowDetails {

    private String department;

    private String designation;

    private Long assignee;

    private String action;

    private String status;
}