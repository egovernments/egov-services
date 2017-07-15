package org.egov.workflow.web.validation;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ErrorField;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.domain.service.EscalationService;
import org.egov.workflow.util.PgrMasterConstants;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.egov.workflow.web.errorhandlers.Error;
import org.egov.workflow.web.errorhandlers.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class EscalationTimeTypeValidator {
	
	@Autowired
	private EscalationService escalationService;
	
	public List<ErrorResponse> validateServiceGroupRequest(final EscalationTimeTypeReq escalationTimeTypeRequest, boolean action) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(escalationTimeTypeRequest, action);
		errorResponse.setError(error);
		if (!errorResponse.getErrorFields().isEmpty())
			errorResponses.add(errorResponse);
		return errorResponses;
	}

	private Error getError(final EscalationTimeTypeReq escalationTimeTypeRequest, boolean action) {
		final List<ErrorField> errorFields = getErrorFields(escalationTimeTypeRequest, action);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PgrMasterConstants.INVALID_ESCALATIONTIMETYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final EscalationTimeTypeReq escalationTimeTypeRequest, boolean action) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addServiceIdValidationErrors(escalationTimeTypeRequest, errorFields);
		addTeanantIdValidationErrors(escalationTimeTypeRequest, errorFields);
		if(action) { 
			checkRecordExists(escalationTimeTypeRequest, errorFields);
		}
		return errorFields;
	}

	private void addServiceIdValidationErrors(final EscalationTimeTypeReq escalationTimeTypeRequest,
			final List<ErrorField> errorFields) {
		final EscalationTimeType ecalationTimeType = escalationTimeTypeRequest.getEscalationTimeType();
		if (ecalationTimeType.getGrievanceType().getId() == 0L) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.GRIEVANCETYPE_ID_MANDATORY_CODE)
					.message(PgrMasterConstants.GRIEVANCETYPE_CODE_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.GRIEVANCETYPE_CODE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
		if (ecalationTimeType.getNoOfHours() == 0L) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.NO_0F_HOURS_MANDATORY_CODE)
					.message(PgrMasterConstants.NO_0F_HOURS_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.NO_0F_HOURS_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}
	
	private void addTeanantIdValidationErrors(final EscalationTimeTypeReq escalationTimeTypeRequest,
		final List<ErrorField> errorFields) {
		final EscalationTimeType ecalationTimeType = escalationTimeTypeRequest.getEscalationTimeType();
		if (ecalationTimeType.getTenantId() == null || ecalationTimeType.getTenantId().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.TENANTID_MANDATORY_CODE)
					.message(PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
					.field(PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
	}
	
	private void checkRecordExists(final EscalationTimeTypeReq escalationTimeTypeRequest,
			final List<ErrorField> errorFields) {
			if (escalationService.checkRecordExists(escalationTimeTypeRequest)) {
				final ErrorField errorField = ErrorField.builder().code(PgrMasterConstants.ESCALATION_HOURS_UNIQUE_CODE)
						.message(PgrMasterConstants.ESCALATION_HOURS_UNIQUE_ERROR_MESSAGE)
						.field(PgrMasterConstants.ESCALATION_HOURS_UNIQUE_FIELD_NAME).build();
				errorFields.add(errorField);
			} else
				return;
		}

	public ErrorResponse populateErrors(final BindingResult errors) {
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

}
