package org.egov.workflow.web.adaptor.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class ComplaintStatusSearchErrorAdaptor implements ErrorAdapter<ComplaintStatusSearchCriteria>{

    @Override
    public ErrorResponse adapt(ComplaintStatusSearchCriteria model) {
        return new ErrorResponse(null, getError(model));
    }

    private Error getError(ComplaintStatusSearchCriteria model) {
        List<ErrorField> errorFields = getErrorFields(model);
        return Error.builder()
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
        if(model.isRolesNotPresesnt())
            errorFields.add(new ErrorField("", "User has no roles associated with", ""));
    }

    private void addNameValidationErrors(ComplaintStatusSearchCriteria model, List<ErrorField> errorFields) {
        if(model.isNameNotPresent())
            errorFields.add(new ErrorField("", "Current status is required", ""));
    }
}
