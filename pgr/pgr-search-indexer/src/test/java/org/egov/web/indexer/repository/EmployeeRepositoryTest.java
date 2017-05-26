package org.egov.web.indexer.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.web.indexer.contract.Employee;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class EmployeeRepositoryTest {
	private EmployeeRepository employeeRepository;
	private MockRestServiceServer server;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		employeeRepository = new EmployeeRepository(restTemplate, "http://host/","/hr-employee/employees/_search?positionId={positionId}&asOnDate={asOnDate}&tenantId={tenantId}");
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_employee_for_given_positionid() throws Exception {
		LocalDate sysDate = new LocalDate();
		server.expect(once(), requestTo("http://host/hr-employee/employees/_search?positionId=1&asOnDate="+sysDate.toString("dd/MM/yyyy")+"&tenantId=1"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(new Resources().getFileContents("successEmployeeResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));

		final Employee employee = employeeRepository.fetchEmployeeByPositionId(1L, sysDate, "1");
		server.verify();
		assertEquals(Long.valueOf(1), employee.getId());
		assertEquals("narasappa", employee.getName());
	}

}
