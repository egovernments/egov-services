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
import org.egov.workflow.persistence.repository.AssignmentRepository;
import org.egov.workflow.persistence.repository.PositionRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.persistence.service.StateService;
import org.egov.workflow.persistence.service.WorkFlowMatrixService;
import org.egov.workflow.persistence.service.WorkflowTypesService;
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
import org.egov.workflow.web.contract.User;
import org.egov.workflow.web.contract.UserResponse;
import org.egov.workflow.web.contract.Value;
import org.egov.workflow.web.contract.WorkflowBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowMatrixImpl implements Workflow {

	private static Logger LOG = LoggerFactory.getLogger(WorkflowMatrixImpl.class);
	public static final String SERVICE_CATEGORY_NAME = "serviceCategoryName";
	public static final String APPLICATATIONTYPE_AGREEMENT = "LeaseAgreement";
	public static final String DESIGNATION_COMMISSIONER = "Commissioner";
	public static final String STATUS_FORWARDED = " Forwarded";

	@Autowired
	private StateService stateService;

	@Autowired
	private WorkFlowMatrixService workflowService;

	@Autowired
	private WorkflowTypesService workflowTypeService;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public ProcessInstanceResponse start(ProcessInstanceRequest processInstanceRequest) {
		LOG.info("ProcessInstance Request Payload" + processInstanceRequest.toString());

		RequestInfo requestInfo = processInstanceRequest.getRequestInfo();

		String tenantId = "";

		if (requestInfo != null && requestInfo.getUserInfo().getTenantId() != null
				&& !requestInfo.getUserInfo().getTenantId().isEmpty()) {
			tenantId = requestInfo.getUserInfo().getTenantId();
		} else
			tenantId = processInstanceRequest.getProcessInstance().getTenantId();

		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();

		final WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(processInstance.getBusinessKey(), null, null, null,
				null, null, tenantId);

		Position owner = processInstance.getAssignee();
		if (processInstance.getAssignee() != null && processInstance.getAssignee().getId() != null)
			owner = positionRepository.getById(Long.valueOf(processInstance.getAssignee().getId()), tenantId,
					processInstanceRequest.getRequestInfo());

		final State state = new State();
		state.setTenantId(tenantId);
		state.setType(processInstance.getType());
		state.setSenderName(processInstance.getSenderName());
		state.setStatus(StateStatus.INPROGRESS);
		state.setValue(wfMatrix.getNextState());
		state.setComments(processInstance.getComments());
		if (owner == null) {
			LOG.error("Owner info is not availble from respective service");
			state.setOwnerPosition(processInstance.getAssignee().getId());
		} else {
			state.setOwnerPosition(owner.getId());
		}

		Long userId;
		if (requestInfo.getUserInfo().getTenantId() != null && !requestInfo.getUserInfo().getTenantId().isEmpty()) {
			// TO-DO We need to remove this call to user service for user
			// details, once erp is sending userinfo from user microservice.
			UserResponse userResponse = userRepository.findUserByUserNameAndTenantId(requestInfo);
			userId = userResponse.getUsers().get(0).getId();
		} else {
			userId = requestInfo.getUserInfo().getId();
		}

		if (processInstance.getInitiatorPosition() != null)
			state.setInitiatorPosition(processInstance.getInitiatorPosition());
		else {
			Position initiator = positionRepository.getPrimaryPositionByEmployeeId(userId, tenantId, requestInfo);
			if (initiator != null && initiator.getId() != null)
				state.setInitiatorPosition(initiator.getId());
		}

		state.setNextAction(wfMatrix.getNextAction());
		state.setType(processInstance.getBusinessKey());
		System.out.println("state.getType()---------------------"+state.getType());
	        System.out.println("tenantId---------------------"+tenantId);
		final WorkflowTypes type = workflowTypeService.getWorkflowTypeByTypeAndTenantId(state.getType(), tenantId);
		state.setMyLinkId(type.getLink());

		state.setNatureOfTask(type.getDisplayName());
		state.setExtraInfo(processInstance.getDetails());
		updateAuditDetails(state, processInstanceRequest.getRequestInfo().getUserInfo());
		stateService.create(state);
		processInstance = state.mapToProcess(processInstance);

		ProcessInstanceResponse response = new ProcessInstanceResponse();
		response.setProcessInstance(processInstance);
		return response;
	}

	private void updateAuditDetails(State s, User u) {
		LOG.debug("Updating Logged in user Information... ");
		if( null != s.getId())
		{
			s.setLastModifiedBy(null != u ? u.getId():null);
			s.setLastModifiedDate(new Date());
		}else{
			s.setCreatedBy(null != u ? u.getId():null);
			s.setLastModifiedBy(null != u ? u.getId():null);
			s.setCreatedDate(new Date());
			s.setLastModifiedDate(new Date());
		}
		LOG.debug("Updating Logged in user Information complete. ");
	}

	@Transactional
	@Override
	public TaskResponse update(final TaskRequest taskRequest) {
		LOG.debug("Update task api " + taskRequest.toString());
		Task task = taskRequest.getTask();

		RequestInfo requestInfo = taskRequest.getRequestInfo();
		String tenantId = "";
		String nextState = "";
		String nextAction = "";

		if (requestInfo != null && requestInfo.getUserInfo().getTenantId() != null
				&& !requestInfo.getUserInfo().getTenantId().isEmpty()) {
			tenantId = requestInfo.getUserInfo().getTenantId();
		} else
			tenantId = taskRequest.getTask().getTenantId();

		Position owner = task.getAssignee();
		if (task.getAssignee() != null && task.getAssignee().getId() != null)
			owner = positionRepository.getById(Long.valueOf(task.getAssignee().getId()), tenantId,
					taskRequest.getRequestInfo());
		// final WorkflowEntity entity = task.getEntity();
		String dept = null;
		String currentDesignation = null;
        WorkFlowMatrix wfMatrix =null;
		if (task.getAttributes() != null && task.getAttributes().get("department") != null)
			dept = task.getAttributes().get("department").getCode();

		if (task.getAttributes() != null && task.getAttributes().get("currentDesignation") != null) {
			currentDesignation = task.getValueForKey("currentDesignation");
		}

		if (task.getBusinessKey().endsWith(APPLICATATIONTYPE_AGREEMENT)) {
			wfMatrix = workflowService.getWfMatrix(task.getBusinessKey(), dept, null, null, task.getStatus(), null,
					null, currentDesignation, task.getTenantId());
		} else {
			wfMatrix = workflowService.getWfMatrix(task.getBusinessKey(), dept, null, null, task.getStatus(), null,
					task.getTenantId());
		}

		if (task.getBusinessKey().endsWith(APPLICATATIONTYPE_AGREEMENT)
				&& WorkflowConstants.ACTION_FORWARD.equalsIgnoreCase(task.getAction())) {
			if (currentDesignation != null && currentDesignation.endsWith(DESIGNATION_COMMISSIONER)) {
				nextState = task.getValueForKey("nextState") != null ? task.getValueForKey("nextState")
						: currentDesignation.concat(STATUS_FORWARDED);
				nextAction = task.getValueForKey("nextAction");
			} else {
				nextState = wfMatrix.getNextState();
				nextAction = wfMatrix.getNextAction();
			}

		} else {

			nextState = wfMatrix.getNextState();
			nextAction = wfMatrix.getNextAction();
		}

		final State state = stateService.findByIdAndTenantId(Long.valueOf(task.getId()), tenantId);

		if ("END".equalsIgnoreCase(wfMatrix.getNextAction()))
			state.setStatus(State.StateStatus.ENDED);
		else
			state.setStatus(State.StateStatus.INPROGRESS);

		if (task.getAction().equalsIgnoreCase(WorkflowConstants.ACTION_REJECT)) {
			Long ownerId = state.getInitiatorPosition();
			if (ownerId != null) {
				Position p = Position.builder().id(ownerId).build();
				task.setAssignee(p);
			}
			// below logic required to show the messages only....
			/*
			 * final Attribute approverDesignationName = new Attribute();
			 * approverDesignationName.setCode(owner.getDeptdesig().
			 * getDesignation().getName());
			 * task.getAttributes().put("approverDesignationName",
			 * approverDesignationName);
			 * 
			 * final Attribute approverName = new Attribute();
			 * approverName.setCode(getApproverName(owner));
			 * task.getAttributes().put("approverName", approverName);
			 */
			nextState = "Rejected";
			nextAction = "";
		}
		if (task.getAction().equalsIgnoreCase(WorkflowConstants.ACTION_CANCEL)) {
			state.setStatus(State.StateStatus.ENDED);
			nextState = State.DEFAULT_STATE_VALUE_CLOSED;
		}

		state.addStateHistory(new StateHistory(state));

		state.setTenantId(tenantId);
		state.setValue(nextState);
		state.setComments(task.getComments());
		state.setSenderName(taskRequest.getRequestInfo().getUserInfo().getName());
		if (owner != null && owner.getId() != null)
			state.setOwnerPosition(owner.getId());
		else
			state.setOwnerPosition(state.getInitiatorPosition());
		state.setNextAction(nextAction);
		state.setType(task.getBusinessKey());
		updateAuditDetails(state, requestInfo.getUserInfo());
		if (task.getDetails() != null && !task.getDetails().isEmpty())
			state.setExtraInfo(task.getDetails());
		stateService.create(state);
		Task t = state.map();

		stateService.update(state);
		TaskResponse response = new TaskResponse();
		response.setTask(t);
		LOG.debug("Update task api completed . And response sent back is :" + response.toString());
		return response;
	}

	private String getNextAction(final WorkflowBean workflowBean) {

		WorkFlowMatrix wfMatrix = null;
		if (null != workflowBean && null != workflowBean.getWorkflowId())
			wfMatrix = workflowService.getWfMatrix(workflowBean.getBusinessKey(), workflowBean.getWorkflowDepartment(),
					workflowBean.getAmountRule(), workflowBean.getAdditionalRule(), workflowBean.getCurrentState(),
					workflowBean.getPendingActions(), workflowBean.getCreatedDate(), workflowBean.getTenantId());
		else
			wfMatrix = workflowService.getWfMatrix(workflowBean.getBusinessKey(), workflowBean.getWorkflowDepartment(),
					workflowBean.getAmountRule(), workflowBean.getAdditionalRule(), State.DEFAULT_STATE_VALUE_CREATED,
					workflowBean.getPendingActions(), workflowBean.getCreatedDate(), workflowBean.getTenantId());
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
					workflowBean.getCreatedDate(), workflowBean.getTenantId());
		else if (null != workflowBean.getWorkflowId())
			validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
					workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
					workflowBean.getAdditionalRule(), workflowBean.getCurrentState(), workflowBean.getPendingActions(),
					workflowBean.getCreatedDate(), workflowBean.getTenantId());
		Value v = null;
		for (String s : validActions) {
			v = new Value(s, s);
			values.add(v);
		}
		return values;
	}

	@Override
	public ProcessInstance getProcess(final String jurisdiction, final ProcessInstance processInstance,
			final RequestInfo requestInfo) {

		LOG.debug("Starting getProcess for  " + processInstance.toString() + " for tenant" + jurisdiction);
		final WorkflowBean wfbean = new WorkflowBean();
		processInstance.setTenantId(jurisdiction);
		State state = null;
		if (processInstance.getId() != null && !processInstance.getId().isEmpty())
			state = stateService.findByIdAndTenantId(Long.valueOf(processInstance.getId()),jurisdiction);
		if (state != null) {
			processInstance.setBusinessKey(state.getType());
			if (state.getOwnerPosition() != null)
				processInstance
						.setOwner(positionRepository.getById(state.getOwnerPosition(), jurisdiction, requestInfo));
			else if (state.getOwnerUser() != null)
				processInstance.setOwner(positionRepository.getById(state.getOwnerUser(), jurisdiction, requestInfo));

			if (processInstance.getOwner() == null) {
				Position p = null;
				if (state.getOwnerPosition() != null)
					p = Position.builder().id(state.getOwnerPosition()).build();
				else if (state.getOwnerUser() != null)
					p = Position.builder().id(state.getOwnerUser()).build();
				processInstance.setOwner(p);
			}
			processInstance.setStatus(state.getValue());
			processInstance.setState(state.getStatus().toString());
			processInstance.setSenderName(state.getSenderName());
			processInstance.setComments(state.getComments());
			processInstance.setCreatedDate(state.getCreatedDate());
			processInstance.setLastupdatedSince(state.getLastModifiedDate());
			processInstance.setInitiatorPosition(state.getInitiatorPosition());
		} else {
			throw new NoDataFoundException("ProcessInstance with id " + processInstance.getId() + " not found");
		}
		// processInstance.getEntity().setProcessInstance(processInstance);
		wfbean.map(processInstance);
		if (processInstance.getBusinessKey().endsWith(APPLICATATIONTYPE_AGREEMENT)) {
			wfbean.setPendingActions(state.getNextAction());
		}
		processInstance.setAttributes(new HashMap<>());
		final Attribute validActions = new Attribute();
		validActions.setValues(getValidActions(wfbean));
		processInstance.getAttributes().put("validActions", validActions);
		final Attribute nextAction = new Attribute();
		nextAction.setCode(getNextAction(wfbean));
		processInstance.getAttributes().put("nextAction", nextAction);
		LOG.debug("Starting getProcess complted. And response sent back is " + processInstance);
		return processInstance;
	}

	@Override
	public TaskResponse getTasks(TaskRequest taskRequest) {

		if (LOG.isTraceEnabled())
			LOG.trace("Received task parameters " + taskRequest);

		final List<Task> tasks = new ArrayList<Task>();
		RequestInfo requestInfo = taskRequest.getRequestInfo();
		String tenantId = "";

		if (requestInfo != null && requestInfo.getUserInfo().getTenantId() != null
				&& !requestInfo.getUserInfo().getTenantId().isEmpty()) {
			tenantId = requestInfo.getUserInfo().getTenantId();
		} else
			tenantId = taskRequest.getTask().getTenantId();

		LOG.debug("Starting getTasks for " + taskRequest + " for tenant " + tenantId);

		Long userId;
		if (requestInfo.getUserInfo().getTenantId() != null && !requestInfo.getUserInfo().getTenantId().isEmpty()) {
			// TO-DO We need to remove this call to user service for user
			// details, once erp is sending userinfo from user microservice.
			UserResponse userResponse = userRepository.findUserByUserNameAndTenantId(requestInfo);
			userId = userResponse.getUsers().get(0).getId();
		} else {
			userId = requestInfo.getUserInfo().getId();
		}
		final List<String> types = workflowTypeService.getEnabledWorkflowType(true, tenantId);
		final List<Long> ownerIds = assignmentRepository
				.getByEmployeeId(userId.toString(), taskRequest.getRequestInfo(), new Date()).parallelStream()
				.map(assignment -> assignment.getPosition()).collect(Collectors.toList());
		List<State> states = new ArrayList<State>();
		if (!types.isEmpty())
			states = stateService.getStates(ownerIds, types, userId, tenantId);
		for (final State s : states)
			tasks.add(s.map());

		LOG.debug("getTasks completed for tenant " + tenantId);

		if (LOG.isTraceEnabled())
			LOG.trace("Taks list returned" + tasks);

		TaskResponse response = new TaskResponse();
		response.setTasks(tasks);

		return response;
	}

	@Override
	public List<Task> getHistoryDetail(final String tenantId, final String workflowId) {
		LOG.debug("Starting getHistoryDetail for " + workflowId + " for tenant " + tenantId);
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
		LOG.debug("getHistoryDetail for " + workflowId + " for tenant " + tenantId + "completed.");
		if (LOG.isTraceEnabled()) {
			LOG.trace(tasks.toString());
		}
		return tasks;
	}

	@Override
	public ProcessInstance end(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAssignee(Long locationId, String complaintTypeId, Long assigneeId, RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Designation> getDesignations(Task t, String departmentId) {
		LOG.debug("starting getDesignations " + t + " for department" + departmentId);
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

		String currentState = t.getStatus();
		if ("END".equals(currentState))
			currentState = "";

		List<Designation> nextDesignations = workflowService.getNextDesignations(t.getBusinessKey(), departmentId,
				amtRule, additionalRule, t.getStatus(), pendingAction, new Date(), designation, t.getTenantId());
		LOG.debug("getDesignations completed and returning " + nextDesignations);
		return nextDesignations;
	}

	@Override
	public ProcessInstance update(String jurisdiction, ProcessInstance processInstance) {
		// TODO Auto-generated method stub
		return null;
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
