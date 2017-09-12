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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.service.exception.IdGenerationException;
import org.egov.eis.service.exception.UserException;
import org.egov.eis.web.contract.IdGenerationErrorResponse;
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
					String field = getErrorFieldName(fieldError.getField());
					error.getFields().put(fieldError.getField(), "Field " + field + " Can't Be Left Empty." +
							" Please Provide A Valid Value For The Field - " + field);
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

	public ResponseEntity<?> getResponseEntityForIdGenerationErrors(IdGenerationException idGenerationException) {
		IdGenerationErrorResponse idGenerationErrorResponse = idGenerationException.getIdGenerationErrorResponse();

		ResponseInfo responseInfo;
		Error error = new Error();
		Map<String, Object> fields = new HashMap<>();
		error.setCode(400);

		if (isEmpty(idGenerationErrorResponse)) {
			error.setMessage("Id Generation Request Failed");
			error.setDescription("Unknown Error Occurred In Id Generation Service");
			responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(idGenerationException.getRequestInfo(),
					false);
		} else {
			error.setMessage(idGenerationErrorResponse.getErrors().get(0).getMessage());
			error.setDescription(idGenerationErrorResponse.getErrors().get(0).getDescription());
			responseInfo = idGenerationException.getIdGenerationErrorResponse().getResponseInfo();
		}
		error.setFields(fields);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(error);
		errorResponse.setResponseInfo(responseInfo);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getErrorResponseEntityForNoVacantPositionAvailable(int index, String deptCode,
																							String desigCode, RequestInfo requestInfo) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Sorry, Request For Employee " + (index + 1) + " Failed As Position Is Not Vacant" +
				" For The Department " + deptCode + " & Designation " + desigCode);
		error.setDescription("Error While Processing Bulk Employee Create");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method takes the error field as input for NotNull validated fields & returns the correct field names
	 * for which the error has occurred. So that we can insert those names in field error messages
	 * Eg. if error field is "employee.code", it will return "Code"
	 * Eg. if error field is "employee.user.roles.code", it will return "User Roles Code"
	 * @param field
	 * @return String
	 */
	private String getErrorFieldName(String field) {
		field = WordUtils.capitalize(field.replaceAll("\\[.+\\]", "").replaceAll("\\.", " "));
		if (field.contains(" "))
			return field.substring(field.indexOf(" ") + 1);
		else
			return field;
	}
	
	public ResponseEntity<ErrorResponse> getErrorInvalidData(InvalidDataException ex,
			RequestInfo requestInfo) {
		Error error = new Error();
		
		String message = MessageFormat.format(ex.getMessageKey(),ex.getFieldName(), ex.getFieldValue());
		error.setMessage(message);
		error.setDescription(message);
		error.setCode(400);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
