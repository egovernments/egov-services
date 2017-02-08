package org.egov.workflow.service;

import org.egov.workflow.contract.ProcessInstance;
import org.egov.workflow.entity.State;
import org.egov.workflow.entity.State.StateStatus;
import org.egov.workflow.entity.Task;
import org.egov.workflow.entity.WorkflowTypes;
import org.egov.workflow.model.PositionResponse;
import org.egov.workflow.model.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgrWorkflowImpl implements WorkflowInterface {

    private static final Logger LOG = LoggerFactory.getLogger(PgrWorkflowImpl.class);

    @Autowired
    private ComplaintRouterService complaintRouterService;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @Autowired
    private StateService stateService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Override
    public ProcessInstance start(final String jurisdiction, final ProcessInstance processInstance) {
        Long ownerPosition = null;
        if (processInstance.getAssignee() != null)
            ownerPosition = Long.valueOf(processInstance.getAssignee());
        final State state = new State();
        state.setType(processInstance.getType());
        state.setSenderName(processInstance.getSenderName());
        state.setStatus(StateStatus.INPROGRESS);
        state.setValue("Registered");
        state.setComments(processInstance.getDescription());
        state.setOwnerPosition(ownerPosition);
        state.setNextAction("Waiting for escalation");
        state.setType(processInstance.getBusinessKey());
        final WorkflowTypes type = workflowTypeService.getWorkflowTypeByType(state.getType());
        state.setMyLinkId(type.getLink().replace(":ID", processInstance.getId()));
        state.setNatureOfTask(type.getDisplayName());
        state.setExtraInfo(processInstance.getStateDetails());
        stateService.create(state);
        //if (state.getId() != null) processInstance.setId(state.getId().toString());
        return processInstance;
    }

    @Override
    public ProcessInstance getProcess(final String jurisdiction, final ProcessInstance processInstance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Task> getTasks(final String jurisdiction, final ProcessInstance processInstance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProcessInstance update(final String jurisdiction, final ProcessInstance processInstance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Task update(final String jurisdiction, final Task task) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Task> getHistoryDetail(final String workflowId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PositionResponse getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId) {
        return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId);
    }

    @Override
    public List<Object> getAssignee(final String deptCode, final String designationName) {
        // TODO Auto-generated method stub
        return null;
    }

    private PositionResponse getInitiator(final Long userId) {
        PositionResponse position = null;
        try {
            //position = assignmentService.getPositionsForUser(userId);
        } catch (final Exception e) {
            LOG.error("Error while setting initiator position");
        }
        return position;
    }

    private UserResponse getUserById(final Long userId) {
        UserResponse user = null;
        try {
            user = userService.getUserById(userId);
        } catch (final Exception e) {
            LOG.error("Error while setting initiator position");
        }
        return user;
    }

}
