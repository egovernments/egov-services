package org.egov.workflow.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.Enum.StateStatus;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.persistence.service.ComplaintRouterService;
import org.egov.workflow.persistence.service.StateService;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Designation;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.ProcessInstanceResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.egov.workflow.web.contract.TaskResponse;
import org.egov.workflow.web.contract.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgrWorkflowImpl implements Workflow {

	public static final String STATE_ID = "stateId";
	public static final String DEPARTMENT = "department";
	private final ComplaintRouterService complaintRouterService;
	private final StateService stateService;
	private final EmployeeRepository employeeRepository;
	private final UserRepository userRepository;
	public static final String STATE_DETAILS = "stateDetails";

	@Autowired
	public PgrWorkflowImpl(final ComplaintRouterService complaintRouterService, final StateService stateService,
			final EmployeeRepository employeeRepository, final UserRepository userRepository) {
		this.complaintRouterService = complaintRouterService;
		this.stateService = stateService;
		this.employeeRepository = employeeRepository;
		this.userRepository = userRepository;
	}

	@Override
	public ProcessInstanceResponse start(final ProcessInstanceRequest processInstanceRequest) {
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		final State state = new State();
		state.setType(processInstance.getType());
		state.setSenderName(processInstance.getSenderName());
		//state.setStatus(StateStatus.INPROGRESS);
		state.setStatus(StateStatus.valueOf(StateStatus.class, "INPROGRESS").ordinal());

		state.setValue(processInstance.getStatus());
		state.setComments(processInstance.getComments());
		state.setOwner_pos(resolveAssignee(processInstance, processInstanceRequest.getRequestInfo()));
		state.setExtraInfo(processInstance.getValueForKey("statusDetails"));
		state.setDateInfo(processInstance.getCreatedDate());
		// setAuditableFields(state,
		// Long.valueOf(requestInfo.getRequesterId()));
		stateService.create(state);
		final Value value = new Value(STATE_ID, String.valueOf(state.getId()));
		final List<Value> values = Collections.singletonList(value);
		final Attribute attribute = new Attribute(true, STATE_ID, "String", true, "This is the id of state", values);
		processInstance.getAttributes().put(STATE_ID, attribute);
		Position position = Position.builder().id(processInstance.getAssignee().getId()).build();
		processInstance.setAssignee(position);
		ProcessInstanceResponse response = new ProcessInstanceResponse();
		response.setProcessInstance(processInstance);
		return response;
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

		// ProcessInstance processInstance =
		// processInstanceRequest.getProcessInstance();
		final Long stateId = Long.valueOf(processInstance.getValueForKey(STATE_ID));
		State stateBuild = State.builder()
								.id(stateId)
								.build();
		final State state = stateService.findByIdAndTenantId(stateBuild);
		if (Objects.nonNull(state)) {
			state.addStateHistory(new StateHistory(state));
			//state.setStatus(StateStatus.ENDED);
			state.setStatus(StateStatus.valueOf(StateStatus.class, "ENDED").ordinal());

			state.setValue("closed");
			state.setComments(processInstance.getComments());
			state.setSenderName(processInstance.getSenderName());
			state.setDateInfo(processInstance.getCreatedDate());
			// TODO OWNER POSITION condition to be checked
			/*
			 * UserResponse user = userRepository
			 * .findUserById(Long.valueOf(processInstanceRequest.getRequestInfo(
			 * ).getRequesterId())); if (user.isGrievanceOfficer()) {
			 * state.setOwnerPosition(state.getOwnerPosition()); }
			 */
			// setAuditableFields(state,
			// Long.valueOf(processInstanceRequest.getRequestInfo().getRequesterId()));
			stateService.update(state);
			processInstance.setId(state.getId().toString());
			Position position = Position.builder().id(state.getOwner_pos()).build();
			processInstance.setAssignee(position);
		}
		return processInstance;
	}

	private Long resolveAssignee(final ProcessInstance processInstance, RequestInfo requestInfo) {
		final String complaintTypeCode = processInstance.getValueForKey("complaintTypeCode");
		final Long boundaryId = Long.valueOf(processInstance.getValueForKey("boundaryId"));
		final Long firstTimeAssignee = null;
		final Position response = complaintRouterService.getAssignee(boundaryId, complaintTypeCode, firstTimeAssignee,
				processInstance.getTenantId(), requestInfo);
		return response.getId();
	}

	@Override
	public Position getAssignee(final Long boundaryId, final String complaintTypeCode, final Long assigneeId,
			RequestInfo requestInfo) {
		return complaintRouterService.getAssignee(boundaryId, complaintTypeCode, assigneeId, requestInfo.getTenantId(),requestInfo);
	}

	/*
	 * @Override public List<Task> getHistoryDetail(final String tenantId, final
	 * String workflowId) { final List<Task> tasks = new ArrayList<Task>(); Task
	 * t; final State state = stateService.findOne(Long.valueOf(workflowId));
	 * final Set<StateHistory> history = state.getHistory(); for (final
	 * StateHistory stateHistory : history) { t = stateHistory.map(); Employee
	 * user; User sender = null; if (stateHistory.getLastModifiedBy() > 0)
	 * sender =
	 * userRepository.findUserById(stateHistory.getLastModifiedBy()).getUser().
	 * get(0); if (sender != null) t.setSender(sender.getUserName() + "::" +
	 * sender.getName()); else t.setSender(""); if (stateHistory.getOwnerUser()
	 * != null) {
	 * 
	 * user = employeeRepository.getEmployeeForUserId(state.getOwnerUser()).
	 * getEmployees().get(0); t.setOwner(user.getUsername() + "::" +
	 * user.getName()); final Department dept =
	 * user.getAssignments().get(0).getDepartment();
	 * t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName())); }
	 * else { final Employee emp = employeeRepository
	 * .getEmployeeForPosition(stateHistory.getOwnerPosition(), new
	 * LocalDate()).getEmployees().get(0); t.setOwner(emp.getUsername() + "::" +
	 * emp.getName()); final Department dept =
	 * emp.getAssignments().get(0).getDepartment();
	 * t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName())); }
	 * tasks.add(t); } t = state.map(); Employee user; User sender = null; if
	 * (state.getLastModifiedBy() > 0) sender =
	 * userRepository.findUserById(state.getLastModifiedBy()).getUser().get(0);
	 * if (sender != null) t.setSender(sender.getUserName() + "::" +
	 * sender.getName()); else t.setSender(""); if (state.getOwnerUser() !=
	 * null) { user =
	 * employeeRepository.getEmployeeForUserId(state.getOwnerUser()).
	 * getEmployees().get(0); t.setOwner(user.getUsername() + "::" +
	 * user.getName()); final Department dept =
	 * user.getAssignments().get(0).getDepartment();
	 * t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName())); }
	 * else { final Employee emp =
	 * employeeRepository.getEmployeeForPosition(state.getOwnerPosition(), new
	 * LocalDateTime()) .getEmployees().get(0); t.setOwner(emp.getUsername() +
	 * "::" + emp.getName()); final Department dept =
	 * emp.getAssignments().get(0).getDepartment();
	 * t.getAttributes().put(DEPARTMENT, putDepartmentValues(dept.getName())); }
	 * tasks.add(t); return tasks; }
	 */

	private Attribute putDepartmentValues(final String departmentName) {
		final Value value = new Value(DEPARTMENT, departmentName);
		final List<Value> values = Collections.singletonList(value);
		final Attribute attribute = Attribute.builder().values(values).build();

		return attribute;
	}

	@Override
	public TaskResponse update(final TaskRequest taskRequest) {
		Task task = taskRequest.getTask();
		final Long stateId = Long.valueOf(task.getValueForKey(STATE_ID));
		State stateBuild =State.builder()
							.id(stateId)
							.build();
		final State state = stateService.findByIdAndTenantId(stateBuild);
		if (Objects.nonNull(state)) {
			state.addStateHistory(new StateHistory(state));
			state.setValue(task.getStatus());
			state.setComments(task.getValueForKey("approvalComments"));
			state.setSenderName(task.getSenderName());
			state.setOwner_pos(Long.valueOf(task.getAssignee().getId()));
			state.setExtraInfo(task.getValueForKey(STATE_DETAILS));
			state.setDateInfo(task.getCreatedDate());
			// TODO - Get these values from request info
			state.setLastModifiedBy(00L);
			state.setLastModifiedDate(new Date());
			stateService.update(state);
			if (state.getId() != null) {
				task.setId(state.getId().toString());
				Position position = Position.builder().id(state.getOwner_pos()).build();
				task.setAssignee(position);
			}
		}
		TaskResponse response = new TaskResponse();
		response.setTask(task);
		return response;
	}

	@Override
	public ProcessInstance getProcess(String jurisdiction, ProcessInstance processInstance,
			final RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance update(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
		final List<Task> tasks = new ArrayList<Task>();
		Task t;
		State stateBuild = State.builder()
								.id(Long.valueOf(workflowId))
								.tenantId(tenantId)
								.build();
		final State state = stateService.findByIdAndTenantId(stateBuild);
		final Set<StateHistory> history = state.getHistory();
		for (final StateHistory stateHistory : history) {
			t = stateHistory.map();
			tasks.add(t);
		}
		t = state.map();
		tasks.add(t);
		return tasks;
	}

	@Override
	public List<Designation> getDesignations(Task t, String departmentName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskResponse getTasks(TaskRequest taskRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
