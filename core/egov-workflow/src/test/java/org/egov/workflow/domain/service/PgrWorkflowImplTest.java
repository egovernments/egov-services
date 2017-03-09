package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.Task;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.PositionRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.Assignment;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Department;
import org.egov.workflow.web.contract.Employee;
import org.egov.workflow.web.contract.EmployeeRes;
import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.contract.Role;
import org.egov.workflow.web.contract.User;
import org.egov.workflow.web.contract.UserResponse;
import org.egov.workflow.web.contract.Value;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(MockitoJUnitRunner.class)
public class PgrWorkflowImplTest {

	private static final String TENANT_ID = "tenantId";
	private static final String COMPLAINT_TYPE_CODE = "complaintTypeCode";
	private static final String BOUNDARY_ID = "boundaryId";

	@MockBean
	private PgrWorkflowImpl pgrWorkflowImpl;

	@Mock
	private ComplaintRouterService complaintRouterService;

	@Mock
	private StateService stateService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private PgrWorkflowImpl workflow;

	@Mock
	private PositionRepository positionRepository;

	@Before
	public void before() {
		pgrWorkflowImpl = new PgrWorkflowImpl(complaintRouterService, stateService, employeeRepository, userRepository);
	}

	@Test
	public void testCreateWorkflowByProcessInstanceWithAssigneeNull() {
		final State stateExpected = State.builder().ownerPosition(2L).build();
		final ProcessInstance processInstance = getProcessInstance();
		final PositionResponse position = getPosition();
		when(complaintRouterService.getAssignee(1L, "C001", null)).thenReturn(position);
		when(positionRepository.getById(2L)).thenReturn(position);
		when(stateService.create(new State())).thenReturn(stateExpected);
		final ProcessInstance actualResponse = pgrWorkflowImpl.start("ap.public", processInstance);
		assertEquals(stateExpected.getOwnerPosition(), actualResponse.getAssignee());
	}

	private PositionResponse getPosition() {
		PositionResponse position = new PositionResponse();
		position.setId(2L);
		position.setName("Accounts Officer");
		return position;
	}

	private ProcessInstance getProcessInstance() {
		final Map<String, Attribute> valuesMap = new HashMap<String, Attribute>();
		final RequestInfo requestInfo = new RequestInfo("apiId", "ver", new Date(), "start", "did", "key", "msgId", "1",
				null, "tenantId");
		final Value complaintType = new Value(COMPLAINT_TYPE_CODE, "C001");
		final Value boundary = new Value(BOUNDARY_ID, "1");
		final List<Value> value1 = Collections.singletonList(complaintType);
		final Attribute attributeComplaintType = new Attribute(false, "", "", true, "", value1);
		final List<Value> value2 = Collections.singletonList(boundary);
		final Attribute attributeBondary = new Attribute(false, "", "", true, "", value2);
		valuesMap.put(COMPLAINT_TYPE_CODE, attributeComplaintType);
		valuesMap.put(BOUNDARY_ID, attributeBondary);
		final ProcessInstance processInstance = ProcessInstance.builder().requestInfo(requestInfo)
				.senderName("narasappa").status("Registered").description("Complaint is registered")
				.createdDate(new Date()).values(valuesMap).build();
		return processInstance;

	}

	@Test
	public void testShouldCloseWorkflow() throws Exception {
		final State expectedState = new State();
		expectedState.setId(119L);
		expectedState.setComments("Workflow Terminated");

		final RequestInfo requestInfo = RequestInfo.builder().requesterId("67").build();

		final Role role = Role.builder().name("citizen").id(1l).description("CITIZEN").build();

		final User user = User.builder().id(67l).roles(Collections.singleton(role)).build();

		final UserResponse expectedUserRequest = UserResponse.builder().responseInfo(new ResponseInfo())
				.user(Collections.singletonList(user)).build();

		final ProcessInstance expectedProcessInstance = ProcessInstance.builder().requestInfo(requestInfo)
				.type("Complaint").description("Workflow Terminated").assignee(2L)
				.values(new HashMap<String, Attribute>()).businessKey("765").build();
		expectedProcessInstance.setStateId(119L);

		when(stateService.getStateById(119L)).thenReturn(expectedState);

		when(userRepository.findUserById(67L)).thenReturn(expectedUserRequest);

		workflow.end(TENANT_ID, expectedProcessInstance);
	}

	@Test
	public void testShouldGetWorkflowHistoryById() {
		final State state = prepareState();
		final EmployeeRes employeeRes = getEmployee();
		final UserResponse userResponse = getUserResponse();
		when(stateService.getStateById(2l)).thenReturn(state);
		when(employeeRepository.getEmployeeForUserId(1l)).thenReturn(employeeRes);
		when(userRepository.findUserById(1L)).thenReturn(userResponse);
		when(employeeRepository.getEmployeeForPosition(3l, new LocalDate())).thenReturn(employeeRes);
		final List<Task> actualHistory = pgrWorkflowImpl.getHistoryDetail(TENANT_ID, "2");
		assertEquals(3, actualHistory.size());
	}

	private State prepareState() {
		final Set<StateHistory> stateHistory = new HashSet<StateHistory>();
		final State state = State.builder().ownerPosition(3L).id(2L).ownerUser(1L).history(stateHistory).build();
		state.setLastModifiedBy(1L);
		state.addStateHistory(prepareStateHistoryWithOwnerUser(state));
		state.addStateHistory(prepareStateHistoryWithOutOwnerUser(state));
		return state;
	}

	private EmployeeRes getEmployee() {
		final EmployeeRes employeeRes = new EmployeeRes();
		final Employee employee = new Employee();
		employee.getAssignments().add(prepareAssignment());
		employee.setName("narasappa");
		employee.setUsername("egovernments");
		employee.setId(1L);
		employee.setCode("001");
		employeeRes.addEmployeeItem(employee);
		return employeeRes;
	}

	private UserResponse getUserResponse() {
		final Role role = Role.builder().name("citizen").id(1l).description("CITIZEN").build();
		final User user = User.builder().id(1l).roles(Collections.singleton(role)).build();

		return UserResponse.builder().responseInfo(new ResponseInfo()).user(Collections.singletonList(user)).build();
	}

	private Assignment prepareAssignment() {
		final Assignment assignment = new Assignment();
		final Department dept = new Department();
		dept.setId(1L);
		dept.setCode("A");
		dept.setName("Accounts");
		assignment.setDepartment(dept);
		return assignment;
	}

	private StateHistory prepareStateHistoryWithOwnerUser(final State state) {
		final StateHistory stateHistory = new StateHistory(state);
		stateHistory.setId(2L);
		return stateHistory;
	}

	private StateHistory prepareStateHistoryWithOutOwnerUser(final State state) {
		final StateHistory stateHistory = new StateHistory(state);
		stateHistory.setId(2L);
		stateHistory.setOwnerUser(null);
		return stateHistory;
	}
}
