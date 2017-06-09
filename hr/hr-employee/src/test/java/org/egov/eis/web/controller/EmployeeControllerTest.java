package org.egov.eis.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.helper.EmployeeHelper;
import org.egov.eis.service.helper.UserSearchURLHelper;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.DataIntegrityValidatorForCreate;
import org.egov.eis.web.validator.DataIntegrityValidatorForUpdate;
import org.egov.eis.web.validator.EmployeeAssignmentValidator;
import org.egov.eis.web.validator.RequestValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    
    @MockBean
    private RequestValidator requestValidator;
    
    @MockBean
	private ErrorHandler errorHandler;

    @MockBean
	private ResponseInfoFactory responseInfoFactory;

    @MockBean
	private EmployeeAssignmentValidator employeeAssignmentValidator;

    @MockBean
	private DataIntegrityValidatorForCreate dataIntegrityValidatorForCreate;

    @MockBean
	private DataIntegrityValidatorForUpdate dataIntegrityValidatorForUpdate;

    @MockBean
	private EmployeeHelper employeeHelper;
    
        @MockBean
        private UserSearchURLHelper userSearchURLHelper;

	@Before
	public void setUp() throws Exception {
	    when(userSearchURLHelper.searchURL(Mockito.anyList(), Mockito.anyString())).thenReturn("");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearch() throws IOException, Exception {
		List<EmployeeInfo> expectedEmployeesList = getExpectedEmployeesForSearch();
		ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
		when(employeeService.getEmployees(any(EmployeeCriteria.class), any(RequestInfo.class)))
        .thenReturn(expectedEmployeesList);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
		.thenReturn(expectedResponseInfo);
		mockMvc.perform(post("/employees/_search").param("tenantId", "1").param("id", "100")
				.content(getFileContents("RequestInfo.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(
        		getFileContents("employeeSearchResponse1.json")));
	}

	@Test
	public void testGet() throws IOException, Exception {
		Employee expectedEmployee = getExpectedEmployeeForGet();
		ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
		when(employeeService.getEmployee(any(Long.class), any(String.class), any(RequestInfo.class)))
        .thenReturn(expectedEmployee);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
		.thenReturn(expectedResponseInfo);
		mockMvc.perform(post("/employees/100/_search")
				.content(getFileContents("RequestInfo.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(
        		getFileContents("employeeGetResponse1.json")));
	}
	
	private List<EmployeeInfo> getExpectedEmployeesForSearch() {
		List<EmployeeInfo> employeeInfos = new ArrayList<>();
		Assignment assignment1 = Assignment.builder().id(10L).build();
		List<Assignment> assignments = new ArrayList<>();
		assignments.add(assignment1);
		EmployeeInfo employeeInfo1 = EmployeeInfo.builder().id(100L).code("00100").assignments(assignments).build();
		employeeInfos.add(employeeInfo1);
		return employeeInfos;
	}
	
	private Employee getExpectedEmployeeForGet() {
		Assignment assignment1 = Assignment.builder().id(10L).build();
		List<Assignment> assignments = new ArrayList<>();
		assignments.add(assignment1);
		Employee employee = Employee.builder().id(100L).code("00100").assignments(assignments).build();
		return employee;
	}
	
	private Employee getExpectedEmployeeForCreate() {
		return getExpectedEmployeeForGet();
	}

	@Test
	public void testCreate() throws IOException, Exception {
		Employee expectedEmployee = getExpectedEmployeeForCreate();
		ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
		when(employeeService.createAsync(any(EmployeeRequest.class)))
        .thenReturn(expectedEmployee);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
		.thenReturn(expectedResponseInfo);
		mockMvc.perform(post("/employees/_create")
				.content(getFileContents("RequestInfo.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(
        		getFileContents("employeeGetResponse1.json"))); // test json is same as that of GET in this test case
	}
	
	@Test
	public void testUpdate() throws IOException, Exception {
		Employee expectedEmployee = getExpectedEmployeeForCreate();
		ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
		when(employeeService.updateAsync(any(EmployeeRequest.class)))
        .thenReturn(expectedEmployee);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
		.thenReturn(expectedResponseInfo);
		mockMvc.perform(post("/employees/_update")
				.content(getFileContents("RequestInfo.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(
        		getFileContents("employeeGetResponse1.json"))); // test json is same as that of GET in this test case
	}

	private String getFileContents(String filePath) throws IOException {
		return new FileUtils().getFileContents(
				"org/egov/eis/web/controller/EmployeeController/" + filePath);
	}
}
