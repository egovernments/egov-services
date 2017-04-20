package org.egov.workflow.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.Task;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Department;
import org.egov.workflow.web.contract.Employee;
import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.User;
import org.egov.workflow.web.contract.UserResponse;
import org.egov.workflow.web.contract.Value;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgrWorkflow implements Workflow {

    public static final String STATE_ID = "stateId";
    public static final String DEPARTMENT = "department";
    public static final String IN_PROGRESS = "IN PROGRESS";
    private final ComplaintRouterService complaintRouterService;
    private final StateService stateService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    public static final String STATE_DETAILS = "stateDetails";

    @Autowired
    public PgrWorkflow(final ComplaintRouterService complaintRouterService, final StateService stateService,
                       final EmployeeRepository employeeRepository, final UserRepository userRepository) {
        this.complaintRouterService = complaintRouterService;
        this.stateService = stateService;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProcessInstance start(final String jurisdiction, final ProcessInstance processInstance) {
        final State state = new State();
        state.setType(processInstance.getType());
        state.setSenderName(processInstance.getSenderName());
        state.setStatus(State.StateStatus.INPROGRESS);
        state.setValue(processInstance.getStatus());
        state.setComments("Complaint is created with CRN : " + processInstance.getValueForKey("crn"));
        state.setNatureOfTask("Grievance");
        state.setOwnerPosition(resolveAssignee(processInstance));
        state.setExtraInfo(processInstance.getValueForKey("statusDetails"));
        state.setDateInfo(processInstance.getCreatedDate());
        setAuditableFields(state, Long.valueOf(processInstance.getRequestInfo().getUserInfo().getId()));
        stateService.create(state);
        final Value value = new Value(STATE_ID, String.valueOf(state.getId()));
        final List<Value> values = Collections.singletonList(value);
        final Attribute attribute = new Attribute(true, STATE_ID, "String", true, "This is the id of state", values);
        processInstance.getValues().put(STATE_ID, attribute);
        processInstance.setAssignee(state.getOwnerPosition());

        return processInstance;
    }

    private void setAuditableFields(State state, Long requesterId) {
        if (state.getCreatedBy() == null)
            state.setCreatedBy(requesterId);
        if (state.getCreatedDate() == null)
            state.setCreatedDate(new Date());
        state.setLastModifiedDate(new Date());
        state.setLastModifiedBy(requesterId);
    }

    @Override
    public ProcessInstance end(final String jurisdiction, final ProcessInstance processInstance) {
        final Long stateId = Long.valueOf(processInstance.getValueForKey(STATE_ID));
        final State state = stateService.getStateById(stateId);
        if (Objects.nonNull(state)) {
            state.addStateHistory(new StateHistory(state));
            state.setStatus(State.StateStatus.ENDED);
            state.setValue(processInstance.getStatus());
            state.setComments(processInstance.getComments());
            state.setSenderName(processInstance.getSenderName());
            state.setDateInfo(processInstance.getCreatedDate());
            // TODO OWNER POSITION condition to be checked
            UserResponse user = userRepository
                .findUserById(Long.valueOf(processInstance.getRequestInfo().getUserInfo().getId()));
            if (user.isGrievanceOfficer()) {
                state.setOwnerPosition(state.getOwnerPosition());
            }
            setAuditableFields(state, Long.valueOf(processInstance.getRequestInfo().getUserInfo().getId()));
            stateService.update(state);
            processInstance.setStateId(state.getId());
            processInstance.setAssignee(state.getOwnerPosition());
        }
        return processInstance;
    }

    private Long resolveAssignee(final ProcessInstance processInstance) {
        final String complaintTypeCode = processInstance.getValueForKey("complaintTypeCode");
        final Long boundaryId = Long.valueOf(processInstance.getValueForKey("boundaryId"));
        final Long firstTimeAssignee = null;
        final PositionResponse response = complaintRouterService.getAssignee(boundaryId, complaintTypeCode,
            firstTimeAssignee);
        return response.getId();
    }

    @Override
    public PositionResponse getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId) {
        return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId);
    }

    @Override
    public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
        final List<Task> tasks = new ArrayList<Task>();
        Task t;
        final State state = stateService.getStateById(Long.valueOf(workflowId));
        final Set<StateHistory> history = state.getHistory();
        for (final StateHistory stateHistory : history) {
            t = stateHistory.map();
            Employee user;
            User sender = null;
            if (stateHistory.getLastModifiedBy() > 0)
                sender = userRepository.findUserById(stateHistory.getLastModifiedBy()).getUser().get(0);
            if (sender != null)
                t.setSender(sender.getUserName() + "::" + sender.getName());
            else
                t.setSender("");
            if (stateHistory.getOwnerUser() != null) {

                user = employeeRepository.getEmployeeForUserId(state.getOwnerUser()).getEmployees().get(0);
                t.setOwner(user.getUsername() + "::" + user.getName());
                final Department dept = user.getAssignments().get(0).getDepartment();
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
            } else {
                final Employee emp = employeeRepository
                    .getEmployeeForPosition(stateHistory.getOwnerPosition(), new LocalDate()).getEmployees().get(0);
                t.setOwner(emp.getUsername() + "::" + emp.getName());
                final Department dept = emp.getAssignments().get(0).getDepartment();
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
            }
            t.setNatureOfTask(stateHistory.getNatureOfTask());
            tasks.add(t);
        }
        t = state.map();
        Employee user;
        User sender = null;
        if (state.getLastModifiedBy() > 0)
            sender = userRepository.findUserById(state.getLastModifiedBy()).getUser().get(0);
        if (sender != null)
            t.setSender(sender.getUserName() + "::" + sender.getName());
        else
            t.setSender("");
        if (state.getOwnerUser() != null) {
            user = employeeRepository.getEmployeeForUserId(state.getOwnerUser()).getEmployees().get(0);
            t.setOwner(user.getUsername() + "::" + user.getName());
            final Department dept = user.getAssignments().get(0).getDepartment();
            t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
        } else {
            final Employee emp = employeeRepository.getEmployeeForPosition(state.getOwnerPosition(), new LocalDate())
                .getEmployees().get(0);
            t.setOwner(emp.getUsername() + "::" + emp.getName());
            final Department dept = emp.getAssignments().get(0).getDepartment();
            t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName()));
        }
        t.setNatureOfTask(state.getNatureOfTask());
        tasks.add(t);
        return tasks;
    }

    private Attribute putDepartmentValues(final String departmentName) {
        final Value value = new Value(DEPARTMENT, departmentName);
        final List<Value> values = Collections.singletonList(value);

        return Attribute.builder().values(values).build();
    }

    @Override
    public Task update(final String tenantId, final Task task) {
        final Long stateId = Long.valueOf(task.getValueForKey(STATE_ID));
        final State state = stateService.getStateById(stateId);
        if (Objects.nonNull(state)) {
            state.addStateHistory(new StateHistory(state));
            state.setValue(task.getStatus());
            state.setComments(task.getValueForKey("approvalComments"));
            state.setSenderName(task.getSender());
            //Logic to handle escalation
            if(null == task.getAssignee()) {
                state.setOwnerPosition(getAssignee(null, task.getComplaintTypeCode(), task.getPreviousAssignee()).getId());
                state.setPreviousOwner(task.getPreviousAssignee());
                state.setValue(IN_PROGRESS);
            }
            else
                state.setOwnerPosition(Long.valueOf(task.getAssignee()));
            state.setExtraInfo(task.getValueForKey(STATE_DETAILS));
            state.setDateInfo(task.getCreatedDate());
            setAuditableFields(state, Long.valueOf(task.getRequestInfo().getUserInfo().getId()));
            stateService.update(state);
            if (state.getId() != null) {
                task.setId(state.getId().toString());
                task.setAssignee(state.getOwnerPosition().toString());
            }
        }
        return task;
    }

}
