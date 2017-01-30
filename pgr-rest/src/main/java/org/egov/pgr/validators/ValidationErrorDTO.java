package org.egov.pgr.validators;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO {

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public ValidationErrorDTO() {

    }

    public void addFieldError(String path, String message) {
        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }

    public void addFieldErrors(List<FieldErrorDTO> errors) {
        this.fieldErrors = errors;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}

