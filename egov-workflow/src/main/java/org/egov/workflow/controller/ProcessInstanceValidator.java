package org.egov.workflow.controller;

import org.egov.workflow.contract.ProcessInstance;
import org.egov.workflow.contract.RequestInfo;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProcessInstanceValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ProcessInstance.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProcessInstance processToValidate = (ProcessInstance) o;
        validateRequestInfo(processToValidate, errors);
        if (processToValidate.getAssignee() == null)
            errors.rejectValue("assignee", "assignee_req", "Assignee required");
        if (processToValidate.getStatus() == null)
            errors.rejectValue("status", "status_req", "Status required");
        if (processToValidate.getType() == null)
            errors.rejectValue("type", "type_req", "Type required");
    }

    private void validateRequestInfo(ProcessInstance processToValidate, Errors errors) {
        RequestInfo requestInfo = processToValidate.getRequestInfo();
        if (requestInfo == null)
            errors.rejectValue("requestInfo", "requestinfo_req", "Header values are required");
        if (requestInfo != null) {
            if (requestInfo.getTenantId() == null)
                errors.rejectValue("requestInfo.tenantId", "tenantid_req", "Tenant id is required");
        }
    }
}
