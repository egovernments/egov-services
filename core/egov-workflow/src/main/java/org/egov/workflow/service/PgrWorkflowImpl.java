package org.egov.workflow.service;

import org.egov.workflow.domain.model.Department;
import org.egov.workflow.domain.model.EmployeeResponse;
import org.egov.workflow.domain.model.PositionResponse;
import org.egov.workflow.domain.model.User;
import org.egov.workflow.domain.service.*;
import org.egov.workflow.repository.entity.State;
import org.egov.workflow.repository.entity.StateHistory;
import org.egov.workflow.repository.entity.Task;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PgrWorkflowImpl implements Workflow {

    public static final String STATE_ID = "stateId";
    public static final String DEPARTMENT = "department";
    private ComplaintRouterService complaintRouterService;
    private StateService stateService;
    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

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
        state.setExtraInfo(processInstance.getValueForKey("statusDetails"));
        state.setDateInfo(processInstance.getCreatedDate());
        //TODO - Get these values from request info
        state.setCreatedBy(00L);
        state.setLastModifiedBy(00L);
        state.setCreatedDate(new Date());
        state.setLastModifiedDate(new Date());
        stateService.create(state);
        Value value = new Value(STATE_ID, String.valueOf(state.getId()));
        List<Value> values = Collections.singletonList(value);
        Attribute attribute = new Attribute(true, STATE_ID, "String", true, "This is the id of state",values);
        processInstance.getValues().put(STATE_ID,attribute);
        processInstance.setAssignee(state.getOwnerPosition());

        return processInstance;
    }

    @Override
    public ProcessInstance end(String jurisdiction,ProcessInstance processInstance) {
        Long stateId = Long.valueOf(processInstance.getValueForKey(STATE_ID));
        final State state = stateService.getStateById(stateId);
        if(Objects.nonNull(state)){
            state.addStateHistory(new StateHistory(state));
            state.setStatus(State.StateStatus.ENDED);
            state.setValue("closed");
            state.setComments(processInstance.getValueForKey("approvalComments"));
            state.setSenderName(processInstance.getSenderName());
            state.setDateInfo(processInstance.getCreatedDate());
            //TODO OWNER POSITION condition to be checked
            if(processInstance.getValueForKey("userRole").equals("Grievance Officer"))
                state.setOwnerPosition(state.getOwnerPosition());
            //TODO - Get these values from request info
            state.setCreatedBy(00L);
            state.setLastModifiedBy(00L);
            state.setCreatedDate(new Date());
            state.setLastModifiedDate(new Date());
            stateService.update(state);
            Value value = new Value(STATE_ID, String.valueOf(state.getId()));
            List<Value> values = Collections.singletonList(value);
            Attribute attribute = new Attribute(true, STATE_ID, "String", true, "This is the id of state",values);
            processInstance.getValues().put(STATE_ID,attribute);
            processInstance.setAssignee(state.getOwnerPosition());
        }
        return processInstance;
    }

    private Long resolveAssignee(ProcessInstance processInstance) {
        String complaintTypeCode = processInstance.getValueForKey("complaintTypeCode");
        Long boundaryId = Long.valueOf(processInstance.getValueForKey("boundaryId"));
        Long firstTimeAssignee = null;
        PositionResponse response = complaintRouterService.getAssignee(boundaryId, complaintTypeCode, firstTimeAssignee);
        return response.getId();
    }

    @Override
    public PositionResponse getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId) {
        return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId);
    }

    @Override
    public List<Task> getHistoryDetail(final String jurisdiction,final String workflowId) {
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
                /*Department dept = departmentService.getDepartmentForUser(user.getId(), new Date());*/
                Department dept = departmentService.getDepartmentForUser();
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
            } else {
                /*EmployeeResponse emp = employeeService.getUserForPosition(stateHistory.getOwnerPosition(), new Date());*/
                EmployeeResponse emp = employeeService.getUserForPosition();
                t.setOwner(emp.getUsername() + "::" + emp.getName());
                /*Department dept = positionService.getDepartmentByPosition(state.getOwnerPosition());*/
                Department dept = positionService.getDepartmentByPosition();
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
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
            /*Department dept = departmentService.getDepartmentForUser(user.getId(), new Date());*/
            Department dept = departmentService.getDepartmentForUser();
            t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
        } else {
            /*EmployeeResponse emp = employeeService.getUserForPosition(stateHistory.getOwnerPosition(), new Date());*/
            EmployeeResponse emp = employeeService.getUserForPosition();
            t.setOwner(emp.getUsername() + "::" + emp.getName());
            /*Department dept = positionService.getDepartmentByPosition(state.getOwnerPosition());*/
            Department dept = positionService.getDepartmentByPosition();
            t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
        }
        tasks.add(t);
        return tasks;
    }

    private Attribute putDepartmentValues(String departmentName){
        Value value = new Value(DEPARTMENT, departmentName);
        List<Value> values = Collections.singletonList(value);
        Attribute attribute = new Attribute().builder()
                .values(values)
                .build();

        return attribute;
    }

}
