package org.egov.workflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egov.workflow.domain.exception.InvalidComplaintStatusSearchException;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ComplaintStatusSearchCriteria {

    private String complaintStatusCode;
    private List<Long> roles;
    private String tenantId;

    public void validate() {
        if (isCodeAbsent() || isRolesAbsent()) {
            throw new InvalidComplaintStatusSearchException(this);
        }
    }

    public boolean isCodeAbsent() {
        return complaintStatusCode==null || complaintStatusCode.isEmpty();
    }

    public boolean isRolesAbsent() {
        return roles == null || roles.isEmpty();
    }
}
