package org.egov.pgrrest.read.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidAttributeEntryException extends RuntimeException {
    private String field;
}

