package org.egov.pgr.domain.exception;


import lombok.Getter;
import org.egov.common.contract.response.ErrorField;

import java.util.List;

@Getter
public class InvalidServiceTypeException extends RuntimeException {

    private List<ErrorField> errorFields;

    public InvalidServiceTypeException(List<ErrorField> errorFields){
        this.errorFields = errorFields;
    }
}
