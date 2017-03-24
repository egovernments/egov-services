package org.egov.workflow.web.validation;

import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.RequestInfo;
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
            errors.rejectValue("assignee", "assignee.required", "Assignee required");
        if (processToValidate.getStatus() == null)
            errors.rejectValue("status", "status.required", "Status required");
        if (processToValidate.getType() == null)
            errors.rejectValue("type", "type.required", "Type required");
    }

    private void validateRequestInfo(ProcessInstance processToValidate, Errors errors) {
      /*  RequestInfo requestInfo = processToValidate.getRequestInfo();
        if (requestInfo == null)
            errors.rejectValue("requestInfo", "requestInfo.required", "Header values are required");
        if (requestInfo != null && requestInfo.getTenantId() == null) {
            errors.rejectValue("requestInfo.tenantId", "tenantId.required", "Tenant id is required");
        }*/
    }
}
