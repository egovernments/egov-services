package org.egov.workflow.web.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ComplaintStatus {

    private Long id;
    private String name;
    private String code;

    public ComplaintStatus(org.egov.workflow.domain.model.ComplaintStatus complaintStatus) {
        this.id = complaintStatus.getId();
        this.name = complaintStatus.getName();
        this.code = complaintStatus.getCode();
    }
}
