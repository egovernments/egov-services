package org.egov.workflow.service;


import org.egov.workflow.model.PositionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgrWorkflowImpl implements WorkflowInterface {

    @Autowired
    private ComplaintRouterService complaintRouterService;

    @Override
    public PositionResponse getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId) {
        return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId);
    }

}
