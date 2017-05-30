/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.lams.web.errorhandlers;

import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.validator.AgreementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

	@ExceptionHandler(value=Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
		e.printStackTrace();
		logger.info("unexpected exception from the application is : "+ e);
		ErrorResponse errorResponse = new ErrorResponse();
		Error error=new Error();
		error.setCode(400);
		error.setMessage(e.getMessage());
		errorResponse.setError(error);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingRequestInfo(RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Missing RequestInfo Parameters");
		error.setDescription("Required RequestInfo Parameter(s) Not Found");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingParameters(BindingResult bindingResult,
			RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Missing Request Parameter");
		error.setDescription("Error While Binding Request");
		if (bindingResult.hasFieldErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
			}
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getResponseEntityForUnexpectedErrors(RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription("Unexpected Error Occurred");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
