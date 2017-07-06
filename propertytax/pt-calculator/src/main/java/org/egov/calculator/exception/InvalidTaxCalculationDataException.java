package org.egov.calculator.exception;

import org.egov.models.RequestInfo;

public class InvalidTaxCalculationDataException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String message;
    private RequestInfo requestInfo;
    private String description;

    public InvalidTaxCalculationDataException(String message, RequestInfo requestInfo,String description) {
        super();
        this.message = message;
        this.requestInfo = requestInfo;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
