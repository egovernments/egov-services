package org.egov.inv.domain;

import lombok.Getter;
import org.egov.common.contract.response.ErrorField;

import java.util.List;

@Getter
public class MaterialException extends RuntimeException {

    private List<ErrorField> errorFields;

    public MaterialException(List<ErrorField> errorFields) {
        this.errorFields = errorFields;
    }
}
