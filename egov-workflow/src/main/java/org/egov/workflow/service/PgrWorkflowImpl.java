package org.egov.workflow.service;


import org.egov.workflow.domain.model.PositionResponse;
import org.egov.workflow.domain.service.ComplaintRouterService;
import org.egov.workflow.repository.entity.State;
import org.egov.workflow.web.contract.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PgrWorkflowImpl implements WorkflowInterface {

    private ComplaintRouterService complaintRouterService;
    private StateService stateService;

    @Autowired
    public PgrWorkflowImpl(ComplaintRouterService complaintRouterService, StateService stateService) {
        this.complaintRouterService = complaintRouterService;
        this.stateService = stateService;
    }

    @Override
    public ProcessInstance start(String jurisdiction, ProcessInstance processInstance) {
        final State state = new State();
        state.setType(processInstance.getType());
        state.setSenderName(processInstance.getSenderName());
        state.setStatus(State.StateStatus.INPROGRESS);
        state.setValue(processInstance.getStatus());
        state.setComments(processInstance.getDescription());
        state.setOwnerPosition(resolveAssignee(processInstance));
        state.setExtraInfo(processInstance.getStateDetails());
        state.setDateInfo(processInstance.getCreatedDate());
        //TODO - Get these values from request info
        state.setCreatedBy(00L);
        state.setLastModifiedBy(00L);
        state.setCreatedDate(new Date());
        state.setLastModifiedDate(new Date());
        stateService.create(state);
        processInstance.setStateId(state.getId());
        processInstance.setAssignee(state.getOwnerPosition());

        return processInstance;
    }

    private Long resolveAssignee(ProcessInstance processInstance) {
        String complaintTypeCode = processInstance.getValues().get("complaint_type_code");
        Long boundaryId = Long.valueOf(processInstance.getValues().get("boundary_id"));
        Long firstTimeAssignee = null;
        PositionResponse response = complaintRouterService.getAssignee(boundaryId, complaintTypeCode, firstTimeAssignee);
        return response.getId();
    }

    @Override
    public PositionResponse getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId) {
        return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId);
    }

}
