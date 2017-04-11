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

    private String complaintStatusName;
    private List<Long> roles;

    public void validate() {
        if (isNameAbsent() || isRolesAbsent()) {
            throw new InvalidComplaintStatusSearchException(this);
        }
    }

    public boolean isNameAbsent() {
        return complaintStatusName==null || complaintStatusName.isEmpty();
    }

    public boolean isRolesAbsent() {
        return roles == null || roles.isEmpty();
    }
}
