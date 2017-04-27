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

package org.egov.eis.web.errorhandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ErrorHandler {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingRequestInfo(BindingResult bindingResult,
			RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Missing RequestBody Fields");
		error.setDescription("Error While Binding RequestBody");
		if (bindingResult.hasFieldErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
			}
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingParameters(BindingResult bindingResult,
			RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Missing Required Query Parameter");
		error.setDescription("Error While Binding ModelAttribute");
		if (bindingResult.hasFieldErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
			}
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getErrorResponseEntityForBindingErrors(BindingResult bindingResult,
			RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Binding Error");
		error.setDescription("Error while binding request object");

		// FIXME : not able to get fieldError error messages. Remove conditions
		// when fixed
		if (bindingResult.hasFieldErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				if (fieldError.getField().contains("Date")
						&& !(fieldError.getField().contains("fromDate") || fieldError.getField().contains("toDate"))) {
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String errorDate = dateFormat.format(fieldError.getRejectedValue());
					error.getFields().put(fieldError.getField(), errorDate);
				} else if (fieldError.getField().contains("code") && fieldError.getRejectedValue() == null) {
					error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
				} else if (fieldError.getField().contains("permanentPincode")
						&& (((String) fieldError.getRejectedValue()).length() > 6)) {
					error.getFields().put(fieldError.getField(),
							"permanentPincode : " + fieldError.getRejectedValue() + " contains more than 6 characters");
				} else if (fieldError.getField().contains("retirementAge")
						|| fieldError.getField().contains("dateOf")) {
					error.getFields().put(fieldError.getField(),
							"Invalid " + fieldError.getField() + " - " + fieldError.getRejectedValue());
				} else if (fieldError.getField().contains("code") || fieldError.getField().contains("passportNo")
						|| fieldError.getField().contains("gpfNo") || fieldError.getField().contains("department")
						|| fieldError.getField().contains("documents")) {
					error.getFields().put(fieldError.getField(), "Duplicate Value - " + fieldError.getField() + " = "
							+ fieldError.getRejectedValue() + " Already Exists");
				} else if (fieldError.getField().contains("fromDate") || fieldError.getField().contains("toDate")) {
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String errorDate = dateFormat.format(fieldError.getRejectedValue());
					error.getFields().put(fieldError.getField(), "Primary Assignments Overlapping : " + errorDate);
				} else if (fieldError.getField().contains("assignments")
						&& (fieldError.getDefaultMessage().contains("no primary assignment"))) {
					error.getFields().put(fieldError.getField(), "No Primary Assignment Provided");
				} else if (fieldError.getField().contains("employee.id")
						&& (fieldError.getDefaultMessage().contains("provide employee id for update"))) {
					error.getFields().put(fieldError.getField(), "provide employee id for update");
				} else if (fieldError.getField().contains("employee.id")
						&& (fieldError.getDefaultMessage().contains("employee id doesn't exist"))) {
					error.getFields().put(fieldError.getField(), "employee id doesn't exist");
				} else if (fieldError.getField().contains("employee.assignment")
						&& (fieldError.getDefaultMessage().contains("assignment doesn't exist"))) {
					error.getFields().put(fieldError.getField(), "assignments doesn't exist for this employee");
				} else if (fieldError.getField().contains("employee.test")
						&& (fieldError.getDefaultMessage().contains("test doesn't exist"))) {
					error.getFields().put(fieldError.getField(), "Departmental test doesn't exist for this employee");
				} else if (fieldError.getField().contains("employee.educationalQualification")
						&& (fieldError.getDefaultMessage().contains("educational doesn't exist"))) {
					error.getFields().put(fieldError.getField(),
							"educationalQualification doesn't exist for this employee");
				} else if (fieldError.getField().contains("employee.probation")
						&& (fieldError.getDefaultMessage().contains("probation test doesn't exist"))) {
					error.getFields().put(fieldError.getField(), "probation doesn't exist for this employee");
				} else if (fieldError.getField().contains("employee.regularisation")
						&& (fieldError.getDefaultMessage().contains("regularisation test doesn't exist"))) {
					error.getFields().put(fieldError.getField(), "regularisation doesn't exist for this employee");
				} else if (fieldError.getField().contains("employee.serviceHistory")
						&& (fieldError.getDefaultMessage().contains("service test doesn't exist"))) {
					error.getFields().put(fieldError.getField(), "serviceHistory doesn't exist for this employee");
				} else if (fieldError.getField().contains("employee.technicalQualification")
						&& (fieldError.getDefaultMessage().contains("technical test doesn't exist"))) {
					error.getFields().put(fieldError.getField(),
							"technicalQualification doesn't exist for this employee");
				} else {
					error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
				}
			}
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
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

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getResponseEntityForExistingUser(EmployeeRequest employeeRequest) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("User Creation Failed");
		error.setDescription("Username Already Exists");
		error.getFields().put("employee.user", employeeRequest.getEmployee().getUser().getUserName());

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(employeeRequest.getRequestInfo(), false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getResponseEntityForUnknownUserDBUpdationError(RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(500);
		error.setMessage("User Creation Failed");
		error.setDescription("Unknown error");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getResponseEntityForUnknownUserUpdationError(RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(500);
		error.setMessage("User Updation Failed");
		error.setDescription("Unknown error");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getResponseEntityForInvalidEmployeeId(BindingResult modelAttributeBindingResult,
			RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Employee id does not exist");
		error.setDescription("Employee id does not exist");
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
