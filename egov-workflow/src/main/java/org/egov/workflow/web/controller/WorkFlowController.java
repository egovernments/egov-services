package org.egov.workflow.web.controller;

import org.egov.workflow.domain.model.PositionResponse;
import org.egov.workflow.domain.service.WorkflowInterface;
import org.egov.workflow.web.contract.AssigneeFilterInfo;
import org.egov.workflow.web.contract.AssigneeRequestInfo;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.validation.ProcessInstanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class WorkFlowController {

    @Autowired
    private WorkflowInterface pgrWorkflowImpl;

    @InitBinder("processInstance")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ProcessInstanceValidator());
    }

    @PostMapping(value = "/assignee", produces = MediaType.APPLICATION_JSON_VALUE)
    public PositionResponse getComplaintAssignee(@RequestBody final AssigneeRequestInfo request) {
        final AssigneeFilterInfo assigneeFilterInfo = request.getAssigneeFilterInfo();

        final PositionResponse positionResponse = (PositionResponse) pgrWorkflowImpl.getAssignee(assigneeFilterInfo.getBoundaryId(),
                assigneeFilterInfo.getComplaintTypeCode(), assigneeFilterInfo.getCurrentAssigneeId());
        if (positionResponse != null)
            positionResponse.setResponseInfo(
                    new ResponseInfo("", "", new Date().toString(), "", "", "Successful response", ""));
        return positionResponse;
    }

}
