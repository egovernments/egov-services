package org.egov.workflow.model;

import org.egov.workflow.contract.ProcessInstance;
import org.egov.workflow.entity.State;
import org.egov.workflow.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateBuilder {

    private final AssignmentService assignmentService;
    private State state;

    @Autowired
    public StateBuilder(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public State build(ProcessInstance processInstance) {
        setWorkFlowInfo(processInstance);
        setInitiator(processInstance);
        return state;
    }

    private void setWorkFlowInfo(ProcessInstance processInstance) {
        state.setType(processInstance.getType());
        state.setValue(processInstance.getStatus());
        state.setSenderName(processInstance.getSenderName());
        state.setDateInfo(processInstance.getCreatedDate());
        state.setOwnerPosition(processInstance.getAssignee());
        state.setComments(processInstance.getDescription());
    }

    private void setInitiator(ProcessInstance processInstance) {
        final PositionResponse position = assignmentService.getPositionsForUser(processInstance.getSenderName());
        state.setInitiatorPosition(position.getId());
    }

}
