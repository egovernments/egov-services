package org.egov.workflow.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.egov.workflow.domain.model.WorkflowConstants;
import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.State.StateStatus;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.WorkFlowMatrix;
import org.egov.workflow.persistence.entity.WorkflowTypes;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.PositionRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.persistence.service.StateService;
import org.egov.workflow.persistence.service.WorkFlowMatrixService;
import org.egov.workflow.persistence.service.WorkflowTypesService;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Employee;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.WorkflowBean;
import org.egov.workflow.web.contract.WorkflowEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowMatrixImpl implements Workflow {

	private static Logger LOG = LoggerFactory.getLogger(WorkflowMatrixImpl.class);
	private List<Object> approverList;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired

	private UserRepository userRepository;
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
	public ProcessInstance start(final String jurisdiction, final ProcessInstance processInstance) {
		final WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(processInstance.getBusinessKey(), null, null, null,
				null, null);
		Position owner = null;
		if (processInstance.getAssignee() != null)
			owner = positionRepository.getById(Long.valueOf(processInstance.getAssignee().getId()));

		String stateDetails = "";
		// fix the below code for
		/*
		 * final WorkflowEntity entity = processInstance.getEntity();
		 * stateDetails=entity.getStateDetails;
		 */
		final State state = new State();
		state.setType(processInstance.getType());
		state.setSenderName(getEmp(1l).getName());
		state.setStatus(StateStatus.INPROGRESS);
		state.setValue(wfMatrix.getNextState());
		state.setComments(processInstance.getDescription());
		state.setOwnerPosition(owner.getId());
		state.setNextAction(wfMatrix.getNextAction());
		state.setType(processInstance.getBusinessKey());
		//state.setInitiatorPosition(getInitiator().getId());
		final WorkflowTypes type = workflowTypeService.getWorkflowTypeByType(state.getType());
		state.setMyLinkId(type.getLink().replace(":ID", type.getLink()));// think
																			// about
																			// this
																			// for
																			// PGR
																			// and
																			// PTIS
		state.setNatureOfTask(type.getDisplayName());
		state.setExtraInfo(stateDetails);
		updateAuditDetails(state);
		stateService.create(state);
		if (state.getId() != null)
			processInstance.setId(state.getId().toString());
		return processInstance;
	}
	
	private void updateAuditDetails(State s)
	{
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
			owner = positionRepository.getById(Long.valueOf(task.getAssignee()));
		// final WorkflowEntity entity = task.getEntity();
		String dept = null;
		if (task.getAttributes()!=null && task.getAttributes().get("department") != null)
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
			if (ownerId != null)
				task.setAssignee(ownerId.toString());
			else
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

		String stateDetails = "";
		// fix the below code for
		/*
		 * final WorkflowEntity entity = processInstance.getEntity();
		 * stateDetails=entity.getStateDetails;
		 */
		state.addStateHistory(new StateHistory(state));

		state.setValue(nextState);
		state.setComments(task.getDescription());
		state.setSenderName(getEmp(1l).getName());
		if (owner != null)
			state.setOwnerPosition(owner.getId());
		state.setNextAction(wfMatrix.getNextAction());
		state.setType(task.getBusinessKey());
		state.setExtraInfo(stateDetails);
		stateService.create(state);
		if (state.getId() != null)
			task.setId(state.getId().toString());
		stateService.update(state);

		return task;
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

	private String getNextAction(final WorkflowEntity model, final WorkflowBean container) {

		WorkFlowMatrix wfMatrix = null;
		if (null != model && null != model.getId())
			if (null != model.getProcessInstance())
				wfMatrix = workflowService.getWfMatrix(model.getProcessInstance().getBusinessKey(),
						container.getWorkflowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
						container.getCurrentState(), container.getPendingActions(), model.getCreatedDate());
			else
				wfMatrix = workflowService.getWfMatrix(model.getProcessInstance().getBusinessKey(),
						container.getWorkflowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
						State.DEFAULT_STATE_VALUE_CREATED, container.getPendingActions(), model.getCreatedDate());
		return wfMatrix == null ? "" : wfMatrix.getNextAction();
	}

	/**
	 * @param model
	 * @param container
	 * @return List of WorkFlow Buttons From Matrix By Passing parametres
	 *         Type,CurrentState,CreatedDate
	 */
	private List<?> getValidActions(final WorkflowEntity model, final WorkflowBean workflowBean) {
		List<String> validActions = Collections.emptyList();
		if (null == model || model.getWorkflowId() == null)
			validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
					workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
					workflowBean.getAdditionalRule(), "NEW", workflowBean.getPendingActions(), model.getCreatedDate());
		else if (null != model.getProcessInstance())
			validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
					workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
					workflowBean.getAdditionalRule(), model.getProcessInstance().getStatus(),
					workflowBean.getPendingActions(), model.getCreatedDate());
		return validActions;
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
				processInstance.setAssignee(positionRepository.getById(state.getOwnerPosition()));
			else if (state.getOwnerUser() != null)
				processInstance.setAssignee(positionRepository.getById(state.getOwnerUser()));
			processInstance.setStatus(state.getValue());
		}
		processInstance.getEntity().setProcessInstance(processInstance);
		wfbean.map(processInstance);
		final Attribute validActions = new Attribute();
		validActions.setVals((List<Object>) getValidActions(processInstance.getEntity(), wfbean));
		processInstance.getAttributes().put("validActions", validActions);
		final Attribute nextAction = new Attribute();
		nextAction.setCode(getNextAction(processInstance.getEntity(), wfbean));
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

	/*
	 * @Override public List<Designation> getDesignations(final Task task, final
	 * String departmentCode) {
	 * 
	 * String currentState = task.getStatus(); final Department deptCode =
	 * departmentService.getDepartmentByCode(departmentCode); final String
	 * departmentRule = deptCode.getName(); final String additionalRule =
	 * task.getAttributes().get("additionalRule").getCode(); final String
	 * amountRule = task.getAttributes().get("amountRule").getCode(); BigDecimal
	 * amtRule = null; if (amountRule != null) amtRule = new
	 * BigDecimal(amountRule); final String type = task.getBusinessKey(); final
	 * String pendingAction = task.getAction(); List<Designation>
	 * designationList; if ("END".equals(currentState)) currentState = "";
	 * designationList = workflowService.getNextDesignations(type,
	 * departmentRule, amtRule, additionalRule, currentState, pendingAction, new
	 * Date()); if (designationList.isEmpty()) designationList =
	 * persistenceService.findAllBy("from Designation"); return designationList;
	 * }
	 * 
	 * @Override public List<Object> getAssignee(final String deptCode, final
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
