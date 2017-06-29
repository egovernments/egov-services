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

package org.egov.pgr.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.model.EscalationHierarchy;
import org.egov.pgr.service.EscalationHierarchyService;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.EscalationHierarchyGetReq;
import org.egov.pgr.web.contract.EscalationHierarchyReq;
import org.egov.pgr.web.contract.EscalationHierarchyRes;
import org.egov.pgr.web.contract.RequestInfoWrapper;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.Error;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.egov.pgr.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/escalation-hierarchy")
public class EscalationHierarchyController {

	private static final Logger logger = LoggerFactory.getLogger(EscalationHierarchyController.class);

	private static final String[] taskAction = { "create", "update" };

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private EscalationHierarchyService escalationHierarchyService;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ErrorHandler errHandler;

	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final EscalationHierarchyReq escalationHierarchyRequest,
			final BindingResult errors) {
		if (errors.hasErrors()) {
			final ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		logger.info("EscalationTimeTypeRequest::" + escalationHierarchyRequest);

		final List<ErrorResponse> errorResponses = validateServiceGroupRequest(escalationHierarchyRequest,
				taskAction[0]);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		final EscalationHierarchy escalationHierarchy = escalationHierarchyService.createEscalationHierarchy(
				applicationProperties.getCreateEscalationHierarchyTopicName(),
				applicationProperties.getCreateEscalationHierarchyTopicKey(), escalationHierarchyRequest);

		final List<EscalationHierarchy> escalationHierarchies = new ArrayList<>();
		escalationHierarchies.add(escalationHierarchy);
		return getSuccessResponse(escalationHierarchies, escalationHierarchyRequest.getRequestInfo());

	}

	@PostMapping(value = "/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final EscalationHierarchyReq escalationHierarchyRequest,
			final BindingResult errors) {
		if (errors.hasErrors()) {
			final ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		logger.info("EscalationTimeTypeRequest::" + escalationHierarchyRequest);

		final List<ErrorResponse> errorResponses = validateServiceGroupRequest(escalationHierarchyRequest,
				taskAction[1]);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		final EscalationHierarchy escalationHierarchy = escalationHierarchyService.updateEscalationHierarchy(
				applicationProperties.getUpdateEscalationHierarchyTopicName(),
				applicationProperties.getUpdateEscalationHierarchyTopicKey(), escalationHierarchyRequest);

		final List<EscalationHierarchy> escalationHierarchies = new ArrayList<>();
		escalationHierarchies.add(escalationHierarchy);
		return getSuccessResponse(escalationHierarchies, escalationHierarchyRequest.getRequestInfo());

	}
	
	@PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final EscalationHierarchyGetReq escHierarchyGetRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<EscalationHierarchy> escHierarchyList = null;
        try {
        	escHierarchyList = escalationHierarchyService.getAllEscalationHierarchy(escHierarchyGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + escHierarchyGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(escHierarchyList, requestInfo);

    }

	private List<ErrorResponse> validateServiceGroupRequest(final EscalationHierarchyReq escalationHierarchyRequest,
			String action) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(escalationHierarchyRequest, action);
		errorResponse.setError(error);
		if (!errorResponse.getErrorFields().isEmpty())
			errorResponses.add(errorResponse);
		return errorResponses;
	}

	private Error getError(final EscalationHierarchyReq escalationHierarchyRequest, String action) {
		final List<ErrorField> errorFields = getErrorFields(escalationHierarchyRequest, action);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PgrMasterConstants.INVALID_ESCALATIONHIERARCHY_REQUEST_MESSAGE).errorFields(errorFields)
				.build();
	}

	private List<ErrorField> getErrorFields(final EscalationHierarchyReq escalationHierarchyRequest, String action) {
		final List<ErrorField> errorFields = new ArrayList<>();
		fromtopositioncheck(escalationHierarchyRequest, errorFields);
		addTeanantIdValidationErrors(escalationHierarchyRequest, errorFields);
		serviceCodeCheck(escalationHierarchyRequest, errorFields);
		if (action.equals(taskAction[0])) {
			checkCombinationExists(escalationHierarchyRequest, errorFields);
		}
		return errorFields;
	}

	private ErrorResponse populateErrors(final BindingResult errors) {
		final ErrorResponse errRes = new ErrorResponse();

		final Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors())
			for (final FieldError fieldError : errors.getFieldErrors())
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
		errRes.setError(error);
		return errRes;
	}

	private void fromtopositioncheck(final EscalationHierarchyReq escalationHierarchyRequest,
			final List<ErrorField> errorFields) {
		for(int i=0 ;i<escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
		if (null == escalationHierarchyRequest.getEscalationHierarchy().get(i).getFromPosition()
				|| escalationHierarchyRequest.getEscalationHierarchy().get(i).getFromPosition() <= 0L) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.FROMPOSITION_MANDATORY_CODE)
					.message(PgrMasterConstants.FROMPOSITION_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.FROMPOSITION_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
		if (null == escalationHierarchyRequest.getEscalationHierarchy().get(i).getToPosition()
				|| escalationHierarchyRequest.getEscalationHierarchy().get(i).getToPosition() <= 0L) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.TOPOSITION_MANDATORY_CODE)
					.message(PgrMasterConstants.TOPOSITION_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.TOPOSITION_MANDATORY_CODE).build();
			errorFields.add(errorField);
		}
		}
	}

	private void serviceCodeCheck(final EscalationHierarchyReq escalationHierarchyRequest,
			final List<ErrorField> errorFields) {
		for(int i=0 ;i<escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
		if (escalationHierarchyRequest.getEscalationHierarchy().get(i).getServiceCode() == null || escalationHierarchyRequest.getEscalationHierarchy().get(i).getServiceCode().isEmpty()) {
			final ErrorField errorField = ErrorField.builder()
					.code(PgrMasterConstants.GRIEVANCETYPE_CODE_MANDATORY_CODE)
					.message(PgrMasterConstants.GRIEVANCETYPE_CODE_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.GRIEVANCETYPE_CODE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
		}
	}

	private void addTeanantIdValidationErrors(final EscalationHierarchyReq escalationHierarchyRequest,
			final List<ErrorField> errorFields) {
		for(int i=0 ;i<escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
		if (escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId() == null || escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.TENANTID_MANDATORY_CODE)
					.message(PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
		}
	}
	
	private void checkCombinationExists(final EscalationHierarchyReq escalationHierarchyRequest,
			final List<ErrorField> errorFields) {
		for(int i=0 ;i<escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
		if (escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId() == null || escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.TENANTID_MANDATORY_CODE)
					.message(PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
		}
	}
	
	private ResponseEntity<?> getSuccessResponse(final List<EscalationHierarchy> escalationHierarchyList,
			final RequestInfo requestInfo) {
		final EscalationHierarchyRes escalationHierarchyRes = new EscalationHierarchyRes();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		escalationHierarchyRes.setResponseInfo(responseInfo);
		escalationHierarchyRes.setEscalationHierarchies(escalationHierarchyList);
		return new ResponseEntity<>(escalationHierarchyRes, HttpStatus.OK);
	}
}
