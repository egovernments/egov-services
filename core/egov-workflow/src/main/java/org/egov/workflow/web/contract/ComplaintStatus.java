package org.egov.workflow.web.contract;

import lombok.Getter;

@Getter
public class ComplaintStatus {

    private Long id;
    private String name;

    public ComplaintStatus(org.egov.workflow.domain.model.ComplaintStatus complaintStatus) {
        this.id = complaintStatus.getId();
        this.name = complaintStatus.getName();
    }
}
