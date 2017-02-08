package org.egov.workflow.model;

import org.egov.workflow.contract.ProcessInstance;
import org.egov.workflow.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StateBuilder {

    private State state;

    @Autowired
    public StateBuilder() {
        this.state = new State();
    }

    public State build(ProcessInstance processInstance) {
        setWorkFlowInfo(processInstance);
        return state;
    }

    private void setWorkFlowInfo(ProcessInstance processInstance) {
        state.setType(processInstance.getType());
        state.setStatus(State.StateStatus.STARTED);
        state.setValue(processInstance.getStatus());
        state.setSenderName(processInstance.getSenderName());
        state.setDateInfo(processInstance.getCreatedDate());
        state.setOwnerPosition(processInstance.getAssignee());
        state.setComments(processInstance.getDescription());
        state.setCreatedDate(new Date());
        state.setLastModifiedDate(new Date());
        //TODO - Remove the constraint in database to not have these fields required
        state.setCreatedBy(000L);
        state.setLastModifiedBy(000L);
    }

}
