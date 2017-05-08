package org.egov.pgrrest.read.web.contract;

import lombok.Getter;

@Getter
public class ComplaintStatus {

    private Long id;
    private String name;

    public ComplaintStatus(org.egov.pgrrest.read.domain.model.ComplaintStatus complaintStatus) {
        this.id = complaintStatus.getId();
        this.name = complaintStatus.getName();
    }
}
