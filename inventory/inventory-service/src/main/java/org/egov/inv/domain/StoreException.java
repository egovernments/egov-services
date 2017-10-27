package org.egov.inv.domain;

import java.util.List;

import org.egov.common.contract.response.ErrorField;

import lombok.Getter;
@Getter
public class StoreException extends RuntimeException {
    
    private List<ErrorField> errorFields;

    public StoreException(List<ErrorField> errorFields) {
        this.errorFields = errorFields;
    }

}
