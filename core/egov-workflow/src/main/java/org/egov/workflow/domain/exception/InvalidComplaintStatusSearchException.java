package org.egov.workflow.domain.exception;

import lombok.Getter;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;

@Getter
public class InvalidComplaintStatusSearchException extends RuntimeException {

    private static final long serialVersionUID = -2391339708359338432L;
    private ComplaintStatusSearchCriteria criteria;

    public InvalidComplaintStatusSearchException(ComplaintStatusSearchCriteria criteria) {
        this.criteria = criteria;
    }
}
