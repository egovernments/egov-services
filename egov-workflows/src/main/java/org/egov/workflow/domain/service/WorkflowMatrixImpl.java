package org.egov.workflow.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.egov.workflow.domain.exception.InvalidDataException;
import org.egov.workflow.domain.exception.NoDataFoundException;
import org.egov.workflow.domain.model.WorkflowConstants;
import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.State.StateStatus;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.WorkFlowMatrix;
import org.egov.workflow.persistence.entity.WorkflowTypes;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.PositionRepository;
import org.egov.workflow.persistence.service.StateService;
import org.egov.workflow.persistence.service.WorkFlowMatrixService;
import org.egov.workflow.persistence.service.WorkflowTypesService;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Designation;
import org.egov.workflow.web.contract.Employee;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.Value;
import org.egov.workflow.web.contract.WorkflowBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowMatrixImpl implements Workflow {

	private static Logger LOG = LoggerFactory.getLogger(WorkflowMatrixImpl.class);
	// private List<Object> approverList;

	@Autowired
	private EmployeeRepository employeeRepository;
	// @Autowired
	// private UserRepository userRepository;
	@Autowired
	private StateService stateService;
	@Autowired
	private WorkFlowMatrixService workflowService;

	@Autowired
	private WorkflowTypesService workflowTypeService;
	@Autowired
	private PositionRepository positionRepository;

	@Transactional
	@Override
	public ProcessInstance start(final String jurisdiction, ProcessInstance processInstance) {
		final WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(processInstance.getBusinessKey(), null, null, null,
				null, null);
		Position owner = null;
		if (processInstance.getAssignee() != null)
			owner = positionRepository.getById(Long.valueOf(processInstance.getAssignee().getId()));

		final State state = new State();
		state.setTenantId(jurisdiction);
		state.setType(processInstance.getType());
		state.setSenderName(getEmp(1l).getName());
		state.setStatus(StateStatus.INPROGRESS);
		state.setValue(wfMatrix.getNextState());
		state.setComments(processInstance.getComments());
		state.setOwnerPosition(owner.getId());
		state.setNextAction(wfMatrix.getNextAction());
		state.setType(processInstance.getBusinessKey());
		// state.setInitiatorPosition(getInitiator().getId());
		final WorkflowTypes type = workflowTypeService.getWorkflowTypeByType(state.getType());
		state.setMyLinkId(type.getLink());// think
		// about
		// this
		// for
		// PGR
		// and
		// PTIS
		state.setNatureOfTask(type.getDisplayName());
		state.setExtraInfo(processInstance.getDetails());
		updateAuditDetails(state);
		stateService.create(state);
		processInstance = state.mapToProcess(processInstance);
		/*
		 * if (state.getId() != null)
		 * processInstance.setId(state.getId().toString());
		 */
		return processInstance;
	}

	private void updateAuditDetails(State s) {
		s.setCreatedBy(1l);
		s.setLastModifiedBy(1l);
		s.setCreatedDate(new Date());
		s.setLastModifiedDate(new Date());
	}

	private Position getInitiator() {
		Position position = null;
		try {

			String code = employeeRepository.getEmployeeForUserId(1l).getEmployees().get(0).getCode();
			List<Position> byEmployeeCode = positionRepository.getByEmployeeCode(code);
			byEmployeeCode.get(0);
		} catch (final Exception e) {
			LOG.error("Error while setting initiator position");
		}
		return position;
	}

	private Employee getEmp(Long userId) {
		Employee emp = null;
		try {

			emp = employeeRepository.getEmployeeForUserId(userId).getEmployees().get(0);

		} catch (final Exception e) {
			LOG.error("Error while setting initiator position");
		}
		return emp;
	}

	@Transactional
	@Override
	public ProcessInstance update(final String jurisdiction, final ProcessInstance pi) {
		return pi;

	}

	@Transactional
	@Override
	public Task update(final String jurisdiction, final Task task) {
		Position owner = null;
		Long ownerId = null;
		if (task.getAssignee() != null)
			owner = positionRepository.getById(Long.valueOf(task.getAssignee().getId()));
		// final WorkflowEntity entity = task.getEntity();
		String dept = null;
		if (task.getAttributes() != null && task.getAttributes().get("department") != null)
			dept = task.getAttributes().get("department").getCode();
		final WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(task.getBusinessKey(), dept, null, null,
				task.getStatus(), null);

		String nextState = wfMatrix.getNextState();
		final State state = stateService.findOne(Long.valueOf(task.getId()));
		if ("END".equalsIgnoreCase(wfMatrix.getNextAction()))
			state.setStatus(State.StateStatus.ENDED);
		else
			state.setStatus(State.StateStatus.INPROGRESS);

		if (task.getAction().equalsIgnoreCase(WorkflowConstants.ACTION_REJECT)) {
			ownerId = state.getInitiatorPosition();
			if (ownerId != null) {
				Position p = new Position();
				p.setId(ownerId);
				task.setAssignee(p);
			} else
				owner = getInitiator();

			// Employee emp =
			// employeeRepository.getEmployeeForUserId(1l).getEmployees().get(0);
			final Attribute approverDesignationName = new Attribute();
			approverDesignationName.setCode(owner.getDeptdesig().getDesignation().getName());
			task.getAttributes().put("approverDesignationName", approverDesignationName);

			final Attribute approverName = new Attribute();
			approverName.setCode(getApproverName(owner));
			task.getAttributes().put("approverName", approverName);
			nextState = "Rejected";
		}
		if (task.getAction().equalsIgnoreCase(WorkflowConstants.ACTION_CANCEL)) {
			state.setStatus(State.StateStatus.ENDED);
			nextState = State.DEFAULT_STATE_VALUE_CLOSED;
		}

		state.addStateHistory(new StateHistory(state));
		
		state.setTenantId(jurisdiction);
		state.setValue(nextState);
		state.setComments(task.getComments());
		state.setSenderName(getEmp(1l).getName());
		if (owner != null)
			state.setOwnerPosition(owner.getId());
		state.setNextAction(wfMatrix.getNextAction());
		state.setType(task.getBusinessKey());
		if (task.getDetails() != null && !task.getDetails().isEmpty())
			state.setExtraInfo(task.getDetails());
		stateService.create(state);
		Task t = state.map();
		/*
		 * if (state.getId() != null) task.setId(state.getId().toString());
		 */
		stateService.update(state);

		return t;
	}

	private String getApproverName(final Position owner) {
		String approverName = null;
		try {
			approverName = getEmp(1l).getName();
		} catch (final Exception e) {
			LOG.error("error while fetching users name");
		}
		return approverName;
	}

	private String getNextAction(final WorkflowBean workflowBean) {

		WorkFlowMatrix wfMatrix = null;
		if (null != workflowBean && null != workflowBean.getWorkflowId())
			wfMatrix = workflowService.getWfMatrix(workflowBean.getBusinessKey(), workflowBean.getWorkflowDepartment(),
					workflowBean.getAmountRule(), workflowBean.getAdditionalRule(), workflowBean.getCurrentState(),
					workflowBean.getPendingActions(), workflowBean.getCreatedDate());
		else
			wfMatrix = workflowService.getWfMatrix(workflowBean.getBusinessKey(), workflowBean.getWorkflowDepartment(),
					workflowBean.getAmountRule(), workflowBean.getAdditionalRule(), State.DEFAULT_STATE_VALUE_CREATED,
					workflowBean.getPendingActions(), workflowBean.getCreatedDate());
		return wfMatrix == null ? "" : wfMatrix.getNextAction();
	}

	/**
	 * @param model
	 * @param container
	 * @return List of WorkFlow Buttons From Matrix By Passing parametres
	 *         Type,CurrentState,CreatedDate
	 */
	private List<Value> getValidActions(final WorkflowBean workflowBean) {
		List<Value> values = new ArrayList<Value>();
		List<String> validActions = Collections.emptyList();
		if (null == workflowBean || workflowBean.getWorkflowId() == null)
			validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
					workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
					workflowBean.getAdditionalRule(), "NEW", workflowBean.getPendingActions(),
					workflowBean.getCreatedDate());
		else if (null != workflowBean.getWorkflowId())
			validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
					workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
					workflowBean.getAdditionalRule(), workflowBean.getCurrentState(), workflowBean.getPendingActions(),
					workflowBean.getCreatedDate());
		Value v = null;
		for (String s : validActions) {
			v = new Value(s, s);
			values.add(v);
		}
		return values;
	}

	@Override
	public ProcessInstance getProcess(final String jurisdiction, final ProcessInstance processInstance) {
		final WorkflowBean wfbean = new WorkflowBean();

		State state = null;
		if (processInstance.getId() != null && !processInstance.getId().isEmpty())
			state = stateService.findOne(Long.valueOf(processInstance.getId()));
		if (state != null) {
			processInstance.setBusinessKey(state.getType());
			if (state.getOwnerPosition() != null)
				processInstance.setOwner(positionRepository.getById(state.getOwnerPosition()));
			else if (state.getOwnerUser() != null)
				processInstance.setOwner(positionRepository.getById(state.getOwnerUser()));
			processInstance.setStatus(state.getValue());
			processInstance.setState(state.getStatus().toString());
			processInstance.setSenderName(state.getSenderName());
			processInstance.setComments(state.getComments());
			processInstance.setCreatedDate(state.getCreatedDate());
			processInstance.setLastupdatedSince(state.getLastModifiedDate());
		} else {
			throw new NoDataFoundException("ProcessInstance with id " + processInstance.getId() + " not found");
		}
		// processInstance.getEntity().setProcessInstance(processInstance);
		wfbean.map(processInstance);
		processInstance.setAttributes(new HashMap<>());
		final Attribute validActions = new Attribute();
		validActions.setValues(getValidActions(wfbean));
		processInstance.getAttributes().put("validActions", validActions);
		final Attribute nextAction = new Attribute();
		nextAction.setCode(getNextAction(wfbean));
		processInstance.getAttributes().put("nextAction", nextAction);

		return processInstance;
	}

	@Override
	public List<Task> getTasks(final String jurisdiction, final ProcessInstance processInstance) {
		final List<Task> tasks = new ArrayList<Task>();
		final Long userId = getEmp(1l).getId();
		final List<String> types = workflowTypeService.getEnabledWorkflowType(false);
		final List<Long> ownerIds = positionRepository
				.getByEmployeeCode(employeeRepository.getEmployeeForUserId(1l).getEmployees().get(0).getCode())
				.parallelStream().map(position -> position.getId()).collect(Collectors.toList());
		List<State> states = new ArrayList<State>();
		if (!types.isEmpty())
			states = stateService.getStates(ownerIds, types, userId);
		for (final State s : states)
			tasks.add(s.map());
		return tasks;
	}

	@Override
	public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
		final List<Task> tasks = new ArrayList<Task>();
		Task t;
		final State state = stateService.findOne(Long.valueOf(workflowId));
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
	public ProcessInstance end(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAssignee(Long locationId, String complaintTypeId, Long assigneeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Designation> getDesignations(Task t, String departmentId) {

		if (t == null) {
			throw new InvalidDataException("Task", "task.required", "Task data is required");
		} else

		if (t.getBusinessKey() == null) {
			throw new InvalidDataException("businessKey", "task.businesskey.required", "businesskey data is required");
		} else if (departmentId == null) {
			throw new InvalidDataException("departmentId", "task.departmentId.required",
					"departmentId data is required");
		}
		Map<String, Attribute> attributes = t.getAttributes();
		String designation = null;
		String pendingAction = null;
		String additionalRule = null;
		String businessRule = null;

		Attribute attribute = attributes.get("businessRule");
		if (attribute != null)
			businessRule = attribute.getCode();
		BigDecimal amtRule = null;
		if (businessRule != null)
			amtRule = new BigDecimal(businessRule);

		attribute = attributes.get("additionalRule");
		if (attribute != null)
			additionalRule = attribute.getCode();

		pendingAction = t.getAction();

		attribute = attributes.get("designation");
		if (attribute != null)
			designation = attribute.getCode();

		String currentState = t.getState();
		if ("END".equals(currentState))
			currentState = "";

		return workflowService.getNextDesignations(t.getBusinessKey(), departmentId, amtRule, additionalRule,
				t.getState(), pendingAction, new Date(), designation);
	}

	/*
	 * * @Override public List<Object> getAssignee(final String deptCode, final
	 * String designationName) { final Department dept =
	 * departmentService.getDepartmentByCode(deptCode); final Long
	 * ApproverDepartmentId = dept.getId(); final Designation desig =
	 * designationService.getDesignationByName(designationName); final Long
	 * DesignationId = desig.getId(); if (DesignationId != null && DesignationId
	 * != -1) { final HashMap<String, String> paramMap = new HashMap<String,
	 * String>(); if (ApproverDepartmentId != null && ApproverDepartmentId !=
	 * -1) paramMap.put("departmentId", ApproverDepartmentId.toString());
	 * paramMap.put("DesignationId", DesignationId.toString()); approverList =
	 * new ArrayList<Object>(); final List<Assignment> assignmentList =
	 * assignmentService
	 * .findAllAssignmentsByDeptDesigAndDates(ApproverDepartmentId,
	 * DesignationId, new Date()); for (final Assignment assignment :
	 * assignmentList) approverList.add(assignment); } return approverList; }
	 */

}
