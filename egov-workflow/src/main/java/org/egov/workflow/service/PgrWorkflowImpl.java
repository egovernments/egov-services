package org.egov.workflow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.egov.workflow.domain.model.Department;
import org.egov.workflow.domain.model.EmployeeResponse;
import org.egov.workflow.domain.model.PositionResponse;
import org.egov.workflow.domain.model.User;
import org.egov.workflow.domain.service.ComplaintRouterService;
import org.egov.workflow.domain.service.DepartmentService;
import org.egov.workflow.domain.service.EmployeeService;
import org.egov.workflow.domain.service.PositionService;
import org.egov.workflow.domain.service.UserService;
import org.egov.workflow.repository.entity.Attribute;
import org.egov.workflow.repository.entity.State;
import org.egov.workflow.repository.entity.StateHistory;
import org.egov.workflow.repository.entity.Task;
import org.egov.workflow.repository.entity.WorkflowTypes;
import org.egov.workflow.web.contract.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgrWorkflowImpl implements Workflow {

    private ComplaintRouterService complaintRouterService;
    private StateService stateService;
    private WorkflowTypeService workflowTypeService;
    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    public PgrWorkflowImpl(ComplaintRouterService complaintRouterService, StateService stateService,
            WorkflowTypeService workflowTypeService) {
        this.complaintRouterService = complaintRouterService;
        this.stateService = stateService;
        this.workflowTypeService = workflowTypeService;
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
        final WorkflowTypes type = workflowTypeService.getWorkflowTypeByType(state.getType());
        state.setNatureOfTask(type.getDisplayName());
        // TODO - Get these values from request info
        state.setCreatedBy(00L);
        state.setLastModifiedBy(00L);
        state.setCreatedDate(new Date());
        state.setLastModifiedDate(new Date());
        stateService.create(state);
        processInstance.setStateId(state.getId());
        processInstance.setAssignee(state.getOwnerPosition());

        return processInstance;
    }

    @Override
    public ProcessInstance end(String jurisdiction, ProcessInstance processInstance) {
        Long stateId = processInstance.getStateId();
        Map<String, String> values = processInstance.getValues();
        final State state = stateService.getStateById(stateId);
        if (Objects.nonNull(state)) {
            state.addStateHistory(new StateHistory(state));
            state.setStatus(State.StateStatus.ENDED);
            state.setValue("closed");
            state.setComments(values.get("approvalComments"));
            // TODO This is logged in username which should be populated
            state.setSenderName(processInstance.getSenderName());
            state.setDateInfo(processInstance.getCreatedDate());
            // TODO OWNER POSITION condition to be checked
            state.setOwnerPosition(state.getOwnerPosition());
            // TODO - Get these values from request info
            state.setCreatedBy(00L);
            state.setLastModifiedBy(00L);
            state.setCreatedDate(new Date());
            state.setLastModifiedDate(new Date());
            stateService.update(state);
            processInstance.setStateId(state.getId());
            processInstance.setAssignee(state.getOwnerPosition());
        }
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

    @Override
    public List<Task> getHistoryDetail(final String workflowId) {
        final List<Task> tasks = new ArrayList<Task>();
        Task t;
        final State state = stateService.getStateById(Long.valueOf(workflowId));
        final Set<StateHistory> history = state.getHistory();
        for (final StateHistory stateHistory : history) {
            t = stateHistory.map();
            User user;
            User sender;
            sender = userService.getById(stateHistory.getLastModifiedBy());
            if (sender != null)
                t.setSender(sender.getUserName() + "::" + sender.getName());
            else
                t.setSender("");
            if (stateHistory.getOwnerUser() != null) {

                user = userService.getById(state.getOwnerUser());
                t.setOwner(user.getUserName() + "::" + user.getName());
                Department dept = departmentService.getDepartmentForUser(user.getId(), new Date());
                Attribute attr = new Attribute();
                attr.setCode("department");
                attr.getValues().add(dept.getName());
                t.getAttributes().put("department", attr);
            } else {
                EmployeeResponse emp = employeeService.getUserForPosition(stateHistory.getOwnerPosition(), new Date());
                t.setOwner(emp.getUsername() + "::" + emp.getName());
                Department dept = positionService.getDepartmentByPosition(state.getOwnerPosition());
                Attribute attr = new Attribute();
                attr.setCode("department");
                attr.getValues().add(dept.getName());
                t.getAttributes().put("department", attr);
            }
            tasks.add(t);
        }
        t = state.map();
        User user;
        User sender;
        sender = userService.getById(state.getLastModifiedBy());
        if (sender != null)
            t.setSender(sender.getUserName() + "::" + sender.getName());
        else
            t.setSender("");
        if (state.getOwnerUser() != null) {
            user = userService.getById(state.getOwnerUser());
            t.setOwner(user.getUserName() + "::" + user.getName());
            Department dept = departmentService.getDepartmentForUser(user.getId(), new Date());
            Attribute attr = new Attribute();
            attr.setCode("department");
            attr.getValues().add(dept.getName());
            t.getAttributes().put("department", attr);
        } else {
            EmployeeResponse emp = employeeService.getUserForPosition(state.getOwnerPosition(), new Date());
            t.setOwner(emp.getUsername() + "::" + emp.getName());
            Department dept = positionService.getDepartmentByPosition(state.getOwnerPosition());
            Attribute attr = new Attribute();
            attr.setCode("department");
            attr.setValues(new ArrayList<String>());
            attr.getValues().add(dept.getName());
            t.getAttributes().put("department", attr);
        }
        tasks.add(t);
        return tasks;
    }

}
