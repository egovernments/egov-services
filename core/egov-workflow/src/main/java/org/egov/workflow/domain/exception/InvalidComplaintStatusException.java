package org.egov.workflow.domain.exception;

import lombok.Getter;
import org.egov.workflow.domain.model.ComplaintStatus;

@Getter
public class InvalidComplaintStatusException extends RuntimeException {

    private static final long serialVersionUID = 8516527278617787682L;
    private ComplaintStatus complaintStatus;

    public InvalidComplaintStatusException(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }
}
