package org.egov.pgr.web.validators;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldErrorDTO {
    private String field;
    private String message;
}