package org.egov.workflow.service;

import org.egov.workflow.domain.model.Department;
import org.egov.workflow.domain.model.Employee;
import org.egov.workflow.domain.model.PositionResponse;
import org.egov.workflow.domain.service.ComplaintRouterService;
import org.egov.workflow.domain.service.EmployeeService;
import org.egov.workflow.repository.entity.State;
import org.egov.workflow.repository.entity.StateHistory;
import org.egov.workflow.repository.entity.Task;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.Value;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PgrWorkflowImpl implements Workflow {

    public static final String STATE_ID = "stateId";
    public static final String DEPARTMENT = "department";
    private ComplaintRouterService complaintRouterService;
    private StateService stateService;
    private EmployeeService employeeService;

    @Autowired
    public PgrWorkflowImpl(ComplaintRouterService complaintRouterService, StateService stateService, EmployeeService employeeService) {
        this.complaintRouterService = complaintRouterService;
        this.stateService = stateService;
        this.employeeService = employeeService;
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
            state.setComments(processInstance.getComments());
            state.setSenderName(processInstance.getSenderName());
            state.setDateInfo(processInstance.getCreatedDate());
            //TODO OWNER POSITION condition to be checked
            if(processInstance.isGrievanceOfficer())
                state.setOwnerPosition(state.getOwnerPosition());
            //TODO - Get these values from request info
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
    public List<Task> getHistoryDetail(final String tenantId,final String workflowId) {
        final List<Task> tasks = new ArrayList<Task>();
        Task t;
        final State state = stateService.getStateById(Long.valueOf(workflowId));
        final Set<StateHistory> history = state.getHistory();
        for (final StateHistory stateHistory : history) {
            t = stateHistory.map();
            Employee user;
			Employee sender;
			sender = employeeService.getEmployeeForUserId(stateHistory.getLastModifiedBy()).getEmployees().get(0);
            if (sender != null)
                t.setSender(sender.getUsername() + "::" + sender.getName());
            else
                t.setSender("");
            if (stateHistory.getOwnerUser() != null) {

            	user = employeeService.getEmployeeForUserId(state.getOwnerUser()).getEmployees().get(0);
                t.setOwner(user.getUsername() + "::" + user.getName());
                Department dept = user.getAssignments().get(0).getDepartment();
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
            } else {
            	Employee emp = employeeService.getEmployeeForPosition(stateHistory.getOwnerPosition(), new LocalDate())
						.getEmployees().get(0);
                t.setOwner(emp.getUsername() + "::" + emp.getName());
                Department dept = emp.getAssignments().get(0).getDepartment();
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
            }
            tasks.add(t);
        }
        t = state.map();
        Employee user;
		Employee sender;
		sender = employeeService.getEmployeeForUserId(state.getLastModifiedBy()).getEmployees().get(0);
        if (sender != null)
            t.setSender(sender.getUsername() + "::" + sender.getName());
        else
            t.setSender("");
        if (state.getOwnerUser() != null) {
        	user = employeeService.getEmployeeForUserId(state.getOwnerUser()).getEmployees().get(0);
            t.setOwner(user.getUsername() + "::" + user.getName());
            Department dept = user.getAssignments().get(0).getDepartment();
            t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
        } else {
        	Employee emp = employeeService.getEmployeeForPosition(state.getOwnerPosition(), new LocalDate()).getEmployees()
					.get(0);
            t.setOwner(emp.getUsername() + "::" + emp.getName());
            Department dept = emp.getAssignments().get(0).getDepartment();
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
