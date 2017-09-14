package org.egov.pgrrest.read.web.controller;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.pgr.common.model.exception.InvalidOtpValidationRequestException;
import org.egov.pgrrest.read.domain.exception.*;
import org.egov.pgrrest.read.web.adapters.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParamsError(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidServiceTypeSearchException.class)
    public ErrorResponse handleInvalidSearchTypeException(InvalidServiceTypeSearchException ex) {
        return new InvalidServiceTypeSearchExceptionAdapter().adapt(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidServiceRequestException.class)
    public ErrorResponse handleInvalidComplaintException(InvalidServiceRequestException ex) {
        return new SevaRequestErrorAdapter().adapt(ex.getComplaint());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidServiceRequestFieldException.class)
    public ErrorResponse handleInvalidComplaintException(InvalidServiceRequestFieldException ex) {
        return new SevaRequestFieldErrorAdapter().adapt(ex.getErrorFields());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceDefinitionNotFoundException.class)
    public ErrorResponse handleInvalidServiceDefinitionCode() {
        return new ServiceDefinitionNotFoundExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidServiceTypeCodeException.class)
    public ErrorResponse handleInvalidServiceTypeCode(InvalidServiceTypeCodeException ex) {
        return new InvalidServiceTypeExceptionAdapter().adapt(ex.getInvalidServiceTypeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OtpValidationNotCompleteException.class)
    public ErrorResponse handleOtpValidationNotCompleteException() {
        return new OtpValidationNotCompleteExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidOtpValidationRequestException.class)
    public ErrorResponse handleInvalidOtpValidationRequestException() {
        return new InvalidOtpValidationRequestExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UpdateServiceRequestNotAllowedException.class)
    public ErrorResponse handleUpdateComplaintException() {
        return new UpdateComplaintNotAllowedExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TenantIdMandatoryException.class)
    public ErrorResponse handleTenantIdMandatoryException() {
        return new TenantIdMandatoryExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceRequestIdMandatoryException.class)
    public ErrorResponse handleServiceRequestIdMandatoryException() {
        return new ServiceRequestIdMandatoryExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAttributeEntryException.class)
    public ErrorResponse handleInvalidAttributeEntryException(InvalidAttributeEntryException ex) {
        return new InvalidAttributeEntryExceptionAdapter().adapt(ex.getField());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDateAttributeEntryException.class)

    public ErrorResponse handleInvalidDateAttributeEntryException(InvalidDateAttributeEntryException ex) {
        return new InvalidDateAttributeEntryExceptionAdapter().adapt(ex.getAttributeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDateTimeAttributeEntryException.class)
    public ErrorResponse handleInvalidDateTimeAttributeEntryException(InvalidDateTimeAttributeEntryException ex) {
        return new InvalidDateTimeAttributeEntryExceptionAdapter().adapt(ex.getAttributeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidIntegerAttributeEntryException.class)
    public ErrorResponse handleInvalidIntegerAttributeEntryException(InvalidIntegerAttributeEntryException ex) {
        return new InvalidIntegerAttributeEntryExceptionAdapter().adapt(ex.getAttributeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDoubleAttributeEntryException.class)
    public ErrorResponse handleInvalidDoubleAttributeEntryException(InvalidDoubleAttributeEntryException ex) {
        return new InvalidDoubleAttributeEntryExceptionAdapter().adapt(ex.getAttributeCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MalformedDraftException.class)
    public ErrorResponse handleMalformedDraftException(MalformedDraftException ex) {
        return new MalformedDraftExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DraftReadException.class)
    public ErrorResponse handleDraftReadException(DraftReadException ex) {
        return new DraftReadExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DraftNotFoundException.class)
    public ErrorResponse handleDraftNotFoundException(DraftNotFoundException ex) {
        return new DraftNotFoundExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MandatoryAttributesAbsentException.class)
    public ErrorResponse handleMandatoryAttributesAbsentException(MandatoryAttributesAbsentException ex) {
        return new MandatoryAttributesAbsentExceptionAdapter().adapt(ex.getMissingMandatoryAttributeCodes());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceStatusNotPresentException.class)
    public ErrorResponse handleServiceStatusNotPresentException() {
        return new ServiceStatusNotPresentExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnknownServiceStatusException.class)
    public ErrorResponse handleUnknownServiceStatusException(UnknownServiceStatusException ex) {
        return new UnknownServiceStatusExceptionAdapter().adapt(ex.getUnknownStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GroupConstraintViolationException.class)
    public ErrorResponse handleGroupConstraintViolationException(GroupConstraintViolationException ex) {
        return new GroupConstraintViolationExceptionAdapter().adapt(ex.getGroupCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidLongAttributeEntryException.class)
    public ErrorResponse handleGroupConstraintViolationException(InvalidLongAttributeEntryException ex) {
        return new InvalidcLongAttributeEntryExceptionAdapter().adapt(ex.getAttributeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTextAreaAttributeEntryException.class)
    public ErrorResponse handleGroupConstraintViolationException(InvalidTextAreaAttributeEntryException ex) {
        return new InvalidTextAreaAttributeEntryExceptionAdapter().adapt(ex.getAttributeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReadException.class)
    public ErrorResponse handleDraftReadException(ReadException ex) {
        return new ReadExceptionAdapter().adapt(null);
    }
}
