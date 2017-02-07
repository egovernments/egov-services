package org.egov.pgr.web.validators;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorDTO {

    private List<FieldErrorDTO> fieldErrors;

    public ValidationErrorDTO() {
        fieldErrors = new ArrayList<>();
    }

    public void addFieldErrors(List<FieldErrorDTO> errors) {
        this.fieldErrors = errors;
    }
}

