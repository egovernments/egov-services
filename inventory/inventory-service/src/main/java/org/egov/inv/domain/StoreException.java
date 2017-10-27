package org.egov.inv.domain;

import lombok.Getter;
import org.egov.common.contract.response.ErrorField;

import java.util.List;

@Getter
public class StoreException extends RuntimeException {

    private List<ErrorField> errorFields;

    public StoreException(List<ErrorField> errorFields) {
        this.errorFields = errorFields;
    }

}
