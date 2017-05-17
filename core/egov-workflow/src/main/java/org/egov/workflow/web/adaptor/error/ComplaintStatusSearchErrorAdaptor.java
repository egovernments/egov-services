package org.egov.workflow.web.adaptor.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class ComplaintStatusSearchErrorAdaptor implements ErrorAdapter<ComplaintStatusSearchCriteria>{

    public static final String INVALID_COMPLAINT_STATUS_SEARCH_CRITERIA_MESSAGE = "Complaint status search criteria has invalid fields.";
    public static final String USER_HAS_NO_ROLES_ASSOCIATED_MESSAGE = "User has no roles associated with.";
    public static final String CURRENT_STATUS_IS_REQUIRED_MESSAGE = "Current status is required.";

    @Override
    public ErrorResponse adapt(ComplaintStatusSearchCriteria model) {
        return new ErrorResponse(null, getError(model));
    }

    private Error getError(ComplaintStatusSearchCriteria model) {
        List<ErrorField> errorFields = getErrorFields(model);
        return Error.builder()
                .message(INVALID_COMPLAINT_STATUS_SEARCH_CRITERIA_MESSAGE)
                .fields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(ComplaintStatusSearchCriteria model) {
        List<ErrorField> errorFields = new ArrayList<>();
        addNameValidationErrors(model, errorFields);
        addRolesValidationErrors(model, errorFields);
        return errorFields;
    }

    private void addRolesValidationErrors(ComplaintStatusSearchCriteria model, List<ErrorField> errorFields) {
        if(!model.isRolesAbsent()) return;

        errorFields.add(
                new ErrorField("", USER_HAS_NO_ROLES_ASSOCIATED_MESSAGE, "")
        );
    }

    private void addNameValidationErrors(ComplaintStatusSearchCriteria model, List<ErrorField> errorFields) {
        if(!model.isCodeAbsent()) return;

        errorFields.add(
                new ErrorField("", CURRENT_STATUS_IS_REQUIRED_MESSAGE, "")
        );
    }
}
