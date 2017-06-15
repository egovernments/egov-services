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

import org.egov.eis.service.exception.UserException;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class ErrorHandler {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public ResponseEntity<ErrorResponse> getErrorResponseEntityForInvalidRequest(BindingResult bindingResult,
				RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Sorry System Can't Process Your Request");
		error.setDescription("Request Data Is Not Valid");

		if (bindingResult.hasFieldErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				if (fieldError.getCode().equals("invalid")) {
					error.getFields().put(fieldError.getField(), fieldError.getDefaultMessage());
				} else {
					System.err.println(fieldError.getField() + " :: " + fieldError.getCode()
							+ " :: " + fieldError.getDefaultMessage() + " :: " + fieldError.getRejectedValue());
					String field = getErrorFieldName(fieldError.getField());
					error.getFields().put(fieldError.getField(), field + " Can't Be Left Empty. Please Provide A Valid " + field);
				}
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

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getResponseEntityForInvalidEmployeeId(RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Sorry System Can't Process Your Request");
		error.setDescription("Employee Id Does Not Exist");
		error.getFields().put("employee.id", "Employee Id Does Not Exist In System. Please Provide The Correct Id.");
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getResponseEntityForUserErrors(UserException userException) {
		UserErrorResponse userErrorResponse = userException.getUserErrorResponse();

		ResponseInfo responseInfo;
		Error error = new Error();
		Map<String, Object> fields = new HashMap<>();
		error.setCode(400);

		if (isEmpty(userErrorResponse)) {
			error.setMessage("User Request Failed");
			error.setDescription("Unknown Error Occurred In User Service");
			fields.put("employee.user", "Unknown Error Occurred While Calling User Service");
			responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(userException.getRequestInfo(),
					false);
		} else {
			error.setMessage(userErrorResponse.getError().getMessage());
			error.setDescription(userErrorResponse.getError().getDescription());
			userErrorResponse.getError().getFields().forEach(field -> {
				field.setField(field.getField().contains("User.") ? field.getField().substring(5) : field.getField());
				String fieldKey = "employee.user." + field.getField();
				Object fieldValue = field.getMessage();
				fields.put(fieldKey, fieldValue);
			});
			responseInfo = userException.getUserErrorResponse().getResponseInfo();
		}
		error.setFields(fields);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(error);
		errorResponse.setResponseInfo(responseInfo);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	private String getErrorFieldName(String field) {
		String f = field.substring(field.lastIndexOf('.') + 1, field.length());
		StringBuilder actualField = new StringBuilder("");
		for (int i = 0; i < f.length(); i++) {
			if (i == 0) {
				actualField.append(Character.toUpperCase(f.charAt(i)));
			} else if (f.charAt(i) >= 65 && f.charAt(i) <= 90) {
				actualField.append(" " + f.charAt(i));
			} else {
				actualField.append(f.charAt(i));
			}
		}
		return actualField.toString();
	}
}
