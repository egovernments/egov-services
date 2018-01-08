package org.egov.lams.web.controller;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.lams.exceptions.CollectionExceedException;
import org.egov.lams.exceptions.NoObjectionRecordsFoundException;
import org.egov.lams.exceptions.NoRenewalRecordsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomExceptionAdviceController {

	private static final String COLLECTION_EXCEED_EXCEPTION = "Rent is fully paid or no demand details found for collection";
	private static final String NO_RENEWAL_RECORDS_EXCEPTION = "No Renewal is started for this agreement.First initiate and complete Renewal";
	private static final String NO_OBJECTION_RECORDS_EXCEPTION = "No Objection is started for this agreement.First initiate and complete Objection";

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CollectionExceedException.class)
	public ErrorResponse handleExceedCollection() {

		final Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value()).message(COLLECTION_EXCEED_EXCEPTION)
				.build();
		return new ErrorResponse(null, error);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoRenewalRecordsFoundException.class)
	public ErrorResponse handleNoRenewalRecordsFound() {
		final Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value()).description(NO_RENEWAL_RECORDS_EXCEPTION).message(NO_RENEWAL_RECORDS_EXCEPTION)
				.build();
		return new ErrorResponse(null, error);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoObjectionRecordsFoundException.class)
	public ErrorResponse handleNoObjectionRecordsFound() {
		final Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value()).description(NO_OBJECTION_RECORDS_EXCEPTION).message(NO_OBJECTION_RECORDS_EXCEPTION)
				.build();
		return new ErrorResponse(null, error);
	}

}
