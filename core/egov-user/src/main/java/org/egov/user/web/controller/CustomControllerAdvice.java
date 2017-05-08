package org.egov.user.web.controller;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.user.domain.exception.*;
import org.egov.user.web.adapters.errors.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserIdMandatoryException.class)
	public ErrorResponse handleUserIdMandatoryException() {
		return new UserIdMandatoryErrorHandler().adapt(null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserProfileUpdateDeniedException.class)
	public ErrorResponse handleUserProfileUpdateDeniedException() {
		return new UserProfileUpdateDeniedErrorHandler().adapt(null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidUserCreateException.class)
	public ErrorResponse handleInvalidUserException(InvalidUserCreateException ex) {
		return new UserRequestErrorAdapter().adapt(ex.getUser());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidUserUpdateException.class)
	public ErrorResponse handleInvalidUserUpdateException(InvalidUserUpdateException ex) {
		return new UserUpdateErrorAdapter().adapt(ex.getUser());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateUserNameException.class)
	public ErrorResponse handleDuplicateUserNameException(DuplicateUserNameException ex) {
		return new DuplicateUserNameErrorHandler().adapt(ex.getUser());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(OtpValidationPendingException.class)
	public ErrorResponse handleInvalidComplaintException() {
		return new OtpValidationErrorAdapter().adapt(null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserNotFoundException.class)
	public ErrorResponse handleUserNotFoundException() {
		return new UserNotFoundErrorHandler().adapt(null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidAccessTokenException.class)
	public ErrorResponse accessTokenException() {
		return new InvalidAccessTokenErrorHandler().adapt();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRoleCodeException.class)
	public ErrorResponse handleInvalidRoleCodeException(InvalidRoleCodeException ex) {
		return new InvalidRoleCodeErrorHandler().adapt(ex.getRoleCode());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserDetailsException.class)
	public ErrorResponse userDetailsException() {
		return new UserDetailsErrorHandler().adapt(null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PasswordMismatchException.class)
	public ErrorResponse handlePasswordMismatchException() {
		return new PasswordMissMatchErrorHandler().adapt(null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidLoggedInUserUpdatePasswordRequestException.class)
	public ErrorResponse handleInvalidUpdatePasswordRequestException(
			InvalidLoggedInUserUpdatePasswordRequestException ex) {
		return new InvalidLoggedInUserUpdatePasswordRequestErrorHandler().adapt(ex.getRequest());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidNonLoggedInUserUpdatePasswordRequestException.class)
	public ErrorResponse handleInvalidNonLoggedInUserUpdatePasswordRequestException(
			InvalidNonLoggedInUserUpdatePasswordRequestException ex) {
		return new InvalidNonLoggedInUserUpdatePasswordRequestErrorHandler().adapt(ex.getModel());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidUserSearchCriteriaException.class)
	public ErrorResponse handleInvalidNonLoggedInUserUpdatePasswordRequestException(
			InvalidUserSearchCriteriaException ex) {
		return new InvalidUserSearchRequestErrorHandler().adapt(ex.getSearchCriteria());
	}

}