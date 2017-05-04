package org.egov.web.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class InvalidCreateMessageRequest extends RuntimeException {
    private List<FieldError> errors;

    public InvalidCreateMessageRequest(List<FieldError> errors) {
        this.errors = errors;
    }
}
