package org.egov.eis.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.TestConfiguration;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.factory.ResponseEntityFactory;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@Import(TestConfiguration.class)
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
	private ResponseEntityFactory responseEntityFactory;

	@MockBean
	private EmployeeAssignmentValidator employeeAssignmentValidator;

	@MockBean
	private ServiceHistoryValidator serviceHistoryValidator;

	@MockBean
	private DataIntegrityValidatorForCreateEmployee dataIntegrityValidatorForCreate;

	@MockBean
	private DataIntegrityValidatorForUpdateEmployee dataIntegrityValidatorForUpdate;

	@Test
	public void testSearch() throws IOException, Exception {
		List<EmployeeInfo> expectedEmployeesList = getExpectedEmployeesForSearch();
		ResponseInfo expectedResponseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324", "20170310130900", "200");
		Map<String, Object> empMap = new HashMap<String, Object>() {{
			put("ResponseInfo", expectedResponseInfo);
			put("Employee", expectedEmployeesList);
		}};
		ResponseEntity<?> responseEntity = new ResponseEntity<>(empMap, HttpStatus.OK);

		when(employeeService.getEmployees(any(EmployeeCriteria.class), any(RequestInfo.class)))
				.thenReturn(expectedEmployeesList);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
				.thenReturn(expectedResponseInfo);
		doReturn(responseEntity).when(responseEntityFactory).getSuccessResponse(anyMapOf(String.class, Object.class), any(RequestInfo.class));

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