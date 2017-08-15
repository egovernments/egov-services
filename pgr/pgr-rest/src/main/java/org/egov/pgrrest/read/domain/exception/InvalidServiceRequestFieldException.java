package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;
import org.egov.common.contract.response.ErrorField;

import java.util.List;

@Getter
public class InvalidServiceRequestFieldException extends RuntimeException {

    private static final long serialVersionUID = -761312648494992125L;
    private List<ErrorField> errorFields;

    public InvalidServiceRequestFieldException(List<ErrorField> errorFields) {
        this.errorFields = errorFields;
    }
}

