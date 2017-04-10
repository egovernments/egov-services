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
        if (isNameNotPresent() || isRolesNotPresesnt()) {
            throw new InvalidComplaintStatusSearchException(this);
        }
    }

    public boolean isNameNotPresent() {
        return complaintStatusName==null || complaintStatusName.isEmpty();
    }

    public boolean isRolesNotPresesnt() {
        return roles == null || roles.isEmpty();
    }
}
