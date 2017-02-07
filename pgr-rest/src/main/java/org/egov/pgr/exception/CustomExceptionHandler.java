package org.egov.pgr.exception;

import org.egov.pgr.contract.ErrorResponse;
import org.egov.pgr.model.Error;
import org.egov.pgr.validators.FieldErrorDTO;
import org.egov.pgr.validators.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParamsError(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidComplaintSearchException.class)
    public ErrorResponse handleInvalidSearchTypeException(Exception ex) {
        return new ErrorResponse(null, new Error(400, ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleAuthenticationError(UnauthorizedAccessException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler
    public ResponseEntity handle(MethodArgumentNotValidException exception) {
        ValidationErrorDTO dto = new ValidationErrorDTO();
        dto.addFieldErrors(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }
}
