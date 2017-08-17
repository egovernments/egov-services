package org.egov.calculator.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvalidPenaltyDataException extends Exception {

    private static final long serialVersionUID = 1L;
    private String customMessage;
    private RequestInfo requestInfo;
    private String description;
}
