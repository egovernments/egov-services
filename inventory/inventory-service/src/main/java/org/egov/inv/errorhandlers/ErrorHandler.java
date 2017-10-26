package org.egov.inv.errorhandlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ErrorHandler {
    
    @Autowired
    ResponseInfoFactory responseInfoFactory;



    public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingRequestInfo(final BindingResult bindingResult,
            final RequestInfo requestInfo) {
        final Error error = new Error();
        error.setCode(400);
        error.setMessage("Missing RequestBody Fields");
        error.setDescription("Error While Binding RequestBody");
        if (bindingResult.hasFieldErrors())
            for (final FieldError fieldError : bindingResult.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());

        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseInfo(responseInfo);
        errorResponse.setError(error);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingParameters(final BindingResult bindingResult,
            final RequestInfo requestInfo) {
        final Error error = new Error();
        error.setCode(400);
        error.setMessage("Missing Required Query Parameter");
        error.setDescription("Error While Binding ModelAttribute");
        if (bindingResult.hasFieldErrors())
            for (final FieldError fieldError : bindingResult.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());

        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseInfo(responseInfo);
        errorResponse.setError(error);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponse> getResponseEntityForUnexpectedErrors(final RequestInfo requestInfo) {
        final Error error = new Error();
        error.setCode(500);
        error.setMessage("Internal Server Error");
        error.setDescription("Unexpected Error Occurred");

        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseInfo(responseInfo);
        errorResponse.setError(error);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ErrorResponse> getErrorResponseEntityForBindingErrors(final BindingResult bindingResult,
            final RequestInfo requestInfo) {
        final Error error = new Error();
        error.setCode(400);
        error.setMessage("Binding Error");
        error.setDescription("Error while binding request object");

        if (bindingResult.hasFieldErrors())
            for (final FieldError fieldError : bindingResult.getFieldErrors())
                if (fieldError.getField().contains("Date")) {
                    final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    final String errorDate = dateFormat.format(fieldError.getRejectedValue());
                    error.getFields().put(fieldError.getField(), errorDate);
                } else
                    error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());

       
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseInfo(responseInfo);
        errorResponse.setError(error);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }
}

