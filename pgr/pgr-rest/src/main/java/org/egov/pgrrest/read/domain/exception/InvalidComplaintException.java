package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

@Getter
public class InvalidComplaintException extends RuntimeException {

    private static final long serialVersionUID = -761312648494992125L;
    private ServiceRequest complaint;

    public InvalidComplaintException(ServiceRequest complaint) {
        this.complaint = complaint;
    }
}
