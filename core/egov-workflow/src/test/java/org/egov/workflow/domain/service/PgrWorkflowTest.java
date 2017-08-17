package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.entity.Task;
import org.egov.workflow.persistence.repository.DepartmentRestRepository;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.PositionRepository;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.*;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(MockitoJUnitRunner.class)
public class PgrWorkflowTest {

	private static final String TENANT_ID = "tenantId";
	private static final String COMPLAINT_TYPE_CODE = "complaintTypeCode";
	private static final String BOUNDARY_ID = "boundaryId";
	public static final String STATE_ID = "systemStateId";
	public static final String STATE_DETAILS = "stateDetails";

	@MockBean
	private PgrWorkflow pgrWorkflow;

	@Mock
	private ComplaintRouterService complaintRouterService;

	@Mock
	private StateService stateService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRestRepository departmentRestRepository;

	@InjectMocks
	private PgrWorkflow workflow;

	@Mock
	private PositionRepository positionRepository;

	@Before
	public void before() {
		pgrWorkflow = new PgrWorkflow(complaintRouterService, stateService, employeeRepository, userRepository,departmentRestRepository);
	}

	private PositionResponse getPosition() {
		PositionResponse position = new PositionResponse();
		position.setId(2L);
		position.setName("Accounts Officer");
		return position;
	}

	private org.egov.common.contract.request.User getUserInfo() {
		return org.egov.common.contract.request.User.builder().id(16L).build();
	}

	private ProcessInstance getProcessInstance() {
		final Map<String, Attribute> valuesMap = new HashMap<String, Attribute>();
		final RequestInfo requestInfo = new RequestInfo();
		requestInfo.setUserInfo(getUserInfo());
		final Value complaintType = new Value(COMPLAINT_TYPE_CODE, "C001");
		final Value boundary = new Value(BOUNDARY_ID, "1");
		final List<Value> value1 = Collections.singletonList(complaintType);
		final Attribute attributeComplaintType = new Attribute(false, "", "", true, "", value1,"tenantId");
		final List<Value> value2 = Collections.singletonList(boundary);
		final Attribute attributeBondary = new Attribute(false, "", "", true, "", value2,"tenantId");
		valuesMap.put(COMPLAINT_TYPE_CODE, attributeComplaintType);
		valuesMap.put(BOUNDARY_ID, attributeBondary);

		final ProcessInstance processInstance = ProcessInstance.builder().requestInfo(requestInfo)
				.senderName("narasappa").status("Registered").description("Complaint is registered")
				.createdDate(new Date()).tenantId("tenantId").values(valuesMap).build();
		return processInstance;

	}

	@Test
	public void testForStartWorkflowWhenAssigneeIdNull() {
		final State stateExpected = State.builder().ownerPosition(2L).build();
		final ProcessInstance processInstance = getProcessInstance();
		final PositionResponse position = getPosition();
		when(complaintRouterService.getAssignee(1L, "C001", null, "tenantId")).thenReturn(position);
		when(positionRepository.getById(2L, "tenantId")).thenReturn(position);
		when(stateService.create(new State())).thenReturn(stateExpected);

		final ProcessInstance actualResponse = pgrWorkflow.start("tenantId", processInstance);

		assertEquals(stateExpected.getOwnerPosition(), actualResponse.getAssignee());
	}

	@Test
	public void testForEndWorkflow() throws Exception {
		final State expectedState = new State();
        expectedState.setId(119L);
        expectedState.setComments("Workflow Terminated");

        final RequestInfo requestInfo = RequestInfo.builder().userInfo(getUserInfo()).build();
        final Role role = Role.builder().name("citizen").id(1l).description("CITIZEN").build();
        final User user = User.builder().id(67L).roles(Collections.singleton(role)).build();
        final UserResponse expectedUserRequest = UserResponse.builder().responseInfo(new ResponseInfo())
                .user(Collections.singletonList(user)).build();
        final ProcessInstance expectedProcessInstance = ProcessInstance.builder().requestInfo(requestInfo)
                .type("Complaint").description("Workflow Terminated").assignee(2L)
                .values(new HashMap<String, Attribute>()).businessKey("765").build();
        expectedProcessInstance.setStateId(119L);

        when(stateService.getStateByIdAndTenantId(119L,TENANT_ID)).thenReturn(expectedState);
        when(userRepository.findUserByIdAndTenantId(16L,TENANT_ID)).thenReturn(expectedUserRequest);

        workflow.end(TENANT_ID, expectedProcessInstance);
	}

	@Test
	public void testForGetWorkflowHistoryByWorkFlowId() {
		final State state = prepareState();
		final EmployeeRes employeeRes = getEmployee();
		final UserResponse userResponse = getUserResponse();
        final DepartmentRes departmentRes=getDepartment();
		when(stateService.getStateByIdAndTenantId(2l,"tenantId")).thenReturn(state);
		when(employeeRepository.getEmployeeForUserIdAndTenantId(1l,"tenantId")).thenReturn(employeeRes);
		when(userRepository.findUserByIdAndTenantId(1L,"tenantId")).thenReturn(userResponse);
		when(employeeRepository.getEmployeeForPositionAndTenantId(3l, new LocalDate(),"tenantId")).thenReturn(employeeRes);
        when(departmentRestRepository.getDepartmentById(1L,"tenantId")).thenReturn(departmentRes);
		final List<Task> actualHistory = pgrWorkflow.getHistoryDetail(TENANT_ID, "2");
		assertEquals(3, actualHistory.size());
	}

	@Test
	public void testForGetWorkflowHistoryWhenWorkFlowIdNull() {
		final State state = prepareStateForWorkflow();
		final EmployeeRes employeeRes = getEmployee();
		final UserResponse userResponse = getUserResponse();
        final DepartmentRes departmentRes=getDepartment();

        when(stateService.getStateByIdAndTenantId(2l,TENANT_ID)).thenReturn(state);
		when(employeeRepository.getEmployeeForUserIdAndTenantId(1l,TENANT_ID)).thenReturn(employeeRes);
		when(userRepository.findUserByIdAndTenantId(1L,TENANT_ID)).thenReturn(userResponse);
		when(employeeRepository.getEmployeeForPositionAndTenantId(3l, new LocalDate(),TENANT_ID)).thenReturn(employeeRes);
        when(departmentRestRepository.getDepartmentById(1L,"tenantId")).thenReturn(departmentRes);

		final List<Task> actualHistory = pgrWorkflow.getHistoryDetail(TENANT_ID, "2");

		assertEquals(3, actualHistory.size());
	}

	@Test
	public void testForUpdateWorkflowWithState() {
		Task task = getTask();
		State state = prepareState();

		when(stateService.update(new State())).thenReturn(state);
		when(stateService.getStateByIdAndTenantId(2l,TENANT_ID)).thenReturn(state);

		Task actualResponse = pgrWorkflow.update("ap.public", task);

		assertEquals(actualResponse.getId(), state.getId().toString());
	}

	@Test
	public void testForUpdateWorkflowForEscalation(){
        Task task = getTaskForEscalation();
        State state = prepareState();
        PositionResponse expectedPositionResponse = PositionResponse.builder()
            .id(3L)
            .name("kiran")
            .build();

        when(stateService.update(new State())).thenReturn(state);
        when(stateService.getStateByIdAndTenantId(2l, "ap.public")).thenReturn(state);
        when(complaintRouterService.getAssignee(null,"",4L,"ap.public")).thenReturn(expectedPositionResponse);

        Task actualResponse = pgrWorkflow.update("ap.public", task);

        assertEquals(actualResponse.getId(), state.getId().toString());
    }

	private UserResponse getUserResponse() {
		final Role role = Role.builder().name("citizen").id(1l).description("CITIZEN").build();
		final User user = User.builder().id(1l).roles(Collections.singleton(role)).build();
		return UserResponse.builder().responseInfo(new ResponseInfo()).user(Collections.singletonList(user)).build();
	}

	private Assignment prepareAssignment() {
		final Assignment assignment=new Assignment();
	    assignment.setDepartment(1L);
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

	private State prepareState() {
		final Set<StateHistory> stateHistory = new HashSet<StateHistory>();
		final State state = State.builder().ownerPosition(3L).id(2L).ownerUser(1L).history(stateHistory).build();

		state.setLastModifiedBy(1L);
		state.addStateHistory(prepareStateHistoryWithOwnerUser(state));
		state.addStateHistory(prepareStateHistoryWithOutOwnerUser(state));

		return state;
	}

	private State prepareStateForWorkflow() {
		final Set<StateHistory> stateHistory = new HashSet<StateHistory>();
		final State state = State.builder().ownerPosition(3L).id(2L).ownerUser(1L).history(stateHistory).build();

		state.setLastModifiedBy(0L);
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

	private DepartmentRes getDepartment(){
        final Department department=Department.builder().id(1L).name("ADMINISTRATION").code("ADM").tenantId("tenantId").build();
        final List<Department> departmentList=new ArrayList<Department>();
        departmentList.add(department);
        return DepartmentRes.builder().department(departmentList).build();
    }

	private Task getTask() {
		final Map<String, Attribute> valuesMap = new HashMap<String, Attribute>();
		final RequestInfo requestInfo = new RequestInfo();
		requestInfo.setUserInfo(getUserInfo());
		final Value stateId = new Value(STATE_ID, "2");
		final Value stateDetails = new Value(STATE_DETAILS, "1");
		final Value comments = new Value("approvalComments", "1");
		final List<Value> value1 = Collections.singletonList(stateId);
		final Attribute attributeStateId = new Attribute(false, "", "", true, "", value1,"tenantId");
		final List<Value> value2 = Collections.singletonList(stateDetails);
		final Attribute attributeStateDetails = new Attribute(false, "", "", true, "", value2,"tenantId");
		final List<Value> value3 = Collections.singletonList(comments);
		final Attribute attributeComments = new Attribute(false, "", "", true, "", value3,"tenantId");

		valuesMap.put(STATE_ID, attributeStateId);
		valuesMap.put(STATE_DETAILS, attributeStateDetails);
		valuesMap.put("approvalComments", attributeComments);
		Task task = Task.builder().attributes(valuesMap).assignee("3").id("2").sender("narasappa").status("PROCESSING")
				.requestInfo(requestInfo).createdDate(new Date()).build();
		return task;
	}

	private Task getTaskForEscalation(){
        final Map<String, Attribute> valuesMap = new HashMap<String, Attribute>();
        final RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(getUserInfo());
        final Value stateId = new Value(STATE_ID, "2");
        final Value stateDetails = new Value(STATE_DETAILS, "1");
        final Value comments = new Value("Complaint escalated", "3");
        final Value currentAssignee = new Value("2", "4");
        final List<Value> value1 = Collections.singletonList(stateId);
        final Attribute attributeStateId = new Attribute(false, "", "", true, "", value1,"ap.public");
        final List<Value> value2 = Collections.singletonList(stateDetails);
        final Attribute attributeStateDetails = new Attribute(false, "", "", true, "", value2,"ap.public");
        final List<Value> value3 = Collections.singletonList(comments);
        final Attribute attributeComments = new Attribute(false, "", "", true, "", value3,"ap.public");
        final List<Value> value4 = Collections.singletonList(currentAssignee);
        final Attribute attributePreviousAssignee = new Attribute(false,"","",true,"",value4,"ap.public");

        valuesMap.put(STATE_ID, attributeStateId);
        valuesMap.put(STATE_DETAILS, attributeStateDetails);
        valuesMap.put("approvalComments", attributeComments);
        valuesMap.put("systemPreviousAssignee",attributePreviousAssignee);

        Task task = Task.builder().attributes(valuesMap).assignee(null).id("2").sender("narasappa").status("PROCESSING")
            .requestInfo(requestInfo).createdDate(new Date()).build();

        return task;
    }
}
