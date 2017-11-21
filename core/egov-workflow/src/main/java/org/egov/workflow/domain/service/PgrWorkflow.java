package org.egov.workflow.domain.service;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.Task;
import org.egov.workflow.persistence.repository.DepartmentRestRepository;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class PgrWorkflow implements Workflow {

    public static final String STATE_ID = "systemStateId";
    public static final String DEPARTMENT = "department";
    public static final String ESCALATED = "ESCALATED";
    public static final String COMPLAINT = "Complaint";
    public static final String SYSTEM_KEYWORD = "keyword";
    public static final String DELIVERABLE_SERVICE = "Deliverable_Service";
    private final ComplaintRouterService complaintRouterService;
    private final StateService stateService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRestRepository departmentRestRepository;

    public static final String STATE_DETAILS = "stateDetails";

    @Autowired
    public PgrWorkflow(final ComplaintRouterService complaintRouterService, final StateService stateService,
                       final EmployeeRepository employeeRepository, final UserRepository userRepository, final DepartmentRestRepository departmentRestRepository) {
        this.complaintRouterService = complaintRouterService;
        this.stateService = stateService;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.departmentRestRepository = departmentRestRepository;
    }

    @Override
    public ProcessInstance start(final String jurisdiction, final ProcessInstance processInstance) {
        final State state = new State();
        state.setType(processInstance.getType());
        state.setSenderName(processInstance.getSenderName());
        state.setStatus(State.StateStatus.INPROGRESS);
        state.setValue(processInstance.getStatus());
        setCommentsByKeyword(processInstance, state);
        state.setNatureOfTask("Service Request");
        state.setOwnerPosition(resolveAssignee(processInstance));
        state.setExtraInfo(processInstance.getValueForKey("systemStatusDetails"));
        state.setDateInfo(processInstance.getCreatedDate());
        state.setTenantId(jurisdiction);
        setAuditableFields(state, Long.valueOf(processInstance.getRequestInfo().getUserInfo().getId()));
        stateService.create(state);
        final Value value = new Value(STATE_ID, String.valueOf(state.getId()));
        final List<Value> values = Collections.singletonList(value);
        final Attribute attribute = new Attribute(true, STATE_ID, "String", true, "This is the id of state", values,
            jurisdiction);
        processInstance.getValues().put(STATE_ID, attribute);
        processInstance.setAssignee(state.getOwnerPosition());

        return processInstance;
    }

    private void setCommentsByKeyword(ProcessInstance processInstance, State state) {
        String keyword = processInstance.getValueForKey(SYSTEM_KEYWORD);
        String crn = processInstance.getValueForKey("crn");
        if (!isEmpty(keyword) && keyword.equalsIgnoreCase(COMPLAINT)) {
            state.setComments("Complaint is Registered with CRN : " + crn);
        }
        if (!isEmpty(keyword) && keyword.equalsIgnoreCase(DELIVERABLE_SERVICE)) {
            state.setComments("Service Request is created with SRN : " + crn);
        }
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
        final State state = stateService.getStateByIdAndTenantId(stateId, jurisdiction);
        if (Objects.nonNull(state)) {
            state.addStateHistory(new StateHistory(state));
            state.setStatus(State.StateStatus.ENDED);
            state.setValue(processInstance.getStatus());
            state.setComments(processInstance.getComments());
            state.setSenderName(processInstance.getSenderName());
            state.setDateInfo(processInstance.getCreatedDate());
            state.setTenantId(jurisdiction);
            // TODO OWNER POSITION condition to be checked
            UserResponse user = userRepository
                .findUserByIdAndTenantId(Long.valueOf(processInstance.getRequestInfo().getUserInfo().getId()), jurisdiction);
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
        String boundaryIdValue = processInstance.getValueForKey("boundaryId");
        Long boundaryId;
        if (processInstance.getValueForKey("boundaryId") == "" || processInstance.getValueForKey("boundaryId") == null) {
            boundaryId = null;
        } else {
            boundaryId = Long.valueOf(boundaryIdValue);
        }
        final Long firstTimeAssignee = null;
        final PositionResponse response = complaintRouterService.getAssignee(boundaryId, complaintTypeCode,
            firstTimeAssignee, processInstance.getTenantId());
        return response.getId();
    }

    @Override
    public PositionResponse getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId, final String tenantId) {
        return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId, tenantId);
    }

    @Override
    public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
        final List<Task> tasks = new ArrayList<Task>();
        Task t;
        final State state = stateService.getStateByIdAndTenantId(Long.valueOf(workflowId), tenantId);
        final Set<StateHistory> history = state.getHistory();
        for (final StateHistory stateHistory : history) {
            t = stateHistory.map();
            Employee user;
            User sender = null;
            if (stateHistory.getLastModifiedBy() > 0){
                UserResponse userResponse = userRepository.findUserByIdAndTenantId(stateHistory.getLastModifiedBy(), tenantId);
                if(userResponse != null && userResponse.getUser() != null && userResponse.getUser().size() > 0)
                    sender = userResponse.getUser().get(0);
            }
            if (sender != null)
                t.setSender(sender.getUserName() + "::" + sender.getName());
            else
                t.setSender("");
            if (stateHistory.getOwnerUser() != null) {
                user = employeeRepository.getEmployeeForUserIdAndTenantId(state.getOwnerUser(), tenantId).getEmployees().get(0);
                t.setOwner(user.getUsername() + "::" + user.getName());
                final Long dept = user.getAssignments().get(0).getDepartment();
                DepartmentRes departmentRes = departmentRestRepository.getDepartmentById(dept, tenantId);
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(departmentRes.getDepartment().get(0).getName()));
            } else {
                final List<Employee> employeeList = employeeRepository
                    .getEmployeeForPositionAndTenantId(stateHistory.getOwnerPosition(), new LocalDate(), tenantId).getEmployees();
                if(!employeeList.isEmpty()){
                    Employee employee = employeeList.get(0);
                    t.setOwner(employee.getUsername() + "::" + employee.getName());
                    final Long dept = employee.getAssignments().get(0).getDepartment();
                    DepartmentRes departmentRes = departmentRestRepository.getDepartmentById(dept, tenantId);
                    t.getAttributes().put(DEPARTMENT, putDepartmentValues(departmentRes.getDepartment().get(0).getName()));
                }
                else{
                    t.setOwner("NO EMPLOYEE Assigned");
                    t.getAttributes().put(DEPARTMENT, putDepartmentValues("NO DEPARTMENT"));
                }
            }
            t.setNatureOfTask(stateHistory.getNatureOfTask());
            tasks.add(t);
        }
        t = state.map();
        Employee user;
        User sender = null;
        if (state.getLastModifiedBy() > 0){
            UserResponse userResponse = userRepository.findUserByIdAndTenantId(state.getLastModifiedBy(), tenantId);
            if(userResponse != null && userResponse.getUser() != null && userResponse.getUser().size() > 0)
                sender = userResponse.getUser().get(0);
        }
        if (sender != null)
            t.setSender(sender.getUserName() + "::" + sender.getName());
        else
            t.setSender("");
        if (state.getOwnerUser() != null) {
            user = employeeRepository.getEmployeeForUserIdAndTenantId(state.getOwnerUser(), tenantId).getEmployees().get(0);
            t.setOwner(user.getUsername() + "::" + user.getName());
            final Long dept = user.getAssignments().get(0).getDepartment();
            DepartmentRes departmentRes = departmentRestRepository.getDepartmentById(dept, tenantId);
            t.getAttributes().put(DEPARTMENT, putDepartmentValues(departmentRes.getDepartment().get(0).getName()));
        } else {
            final List<Employee> employeeList = employeeRepository
                .getEmployeeForPositionAndTenantId(state.getOwnerPosition(), new LocalDate(), tenantId).getEmployees();
            if(!employeeList.isEmpty()){
                Employee employee = employeeList.get(0);
                t.setOwner(employee.getUsername() + "::" + employee.getName());
                final Long dept = employee.getAssignments().get(0).getDepartment();
                DepartmentRes departmentRes = departmentRestRepository.getDepartmentById(dept, tenantId);
                t.getAttributes().put(DEPARTMENT, putDepartmentValues(departmentRes.getDepartment().get(0).getName()));
            }
            else{
                t.setOwner("NO EMPLOYEE Assigned");
                t.getAttributes().put(DEPARTMENT, putDepartmentValues("NO DEPARTMENT"));
            }
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
        final State state = stateService.getStateByIdAndTenantId(stateId, tenantId);
        if (Objects.nonNull(state)) {
            state.addStateHistory(new StateHistory(state));
            state.setValue(task.getStatus());
            state.setComments(task.getValueForKey("systemApprovalComments"));
            state.setSenderName(task.getSender());
            //Logic to handle escalation
            if (null == task.getAssignee()) {
                Long newAssignee = getAssignee(null, task.getComplaintTypeCode(), task.getPreviousAssignee(), tenantId).getId();
                Long previousAssignee = task.getPreviousAssignee();
                if (newAssignee == previousAssignee) {
                    //set assignee because in request it would be null
                    task.setAssignee(String.valueOf(newAssignee));
                    return task;
                } else {
                    state.setOwnerPosition(newAssignee);
                    state.setPreviousOwner(task.getPreviousAssignee());
                    state.setValue(ESCALATED);
                }
            } else
                state.setOwnerPosition(Long.valueOf(task.getAssignee()));
            state.setExtraInfo(task.getValueForKey(STATE_DETAILS));
            state.setDateInfo(task.getCreatedDate());
            state.setTenantId(tenantId);
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
