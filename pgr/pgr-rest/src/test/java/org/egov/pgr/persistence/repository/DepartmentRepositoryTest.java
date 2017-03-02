package org.egov.pgr.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.egov.pgr.web.contract.Department;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentRepositoryTest {

	private static final String HOST = "http://host";
	private static final String DEPARTMENT_BY_ID_URL = "/eis/departments?id=1";
	private static final String ALL_DEPARTMENT_URL = "/eis/departments";

	private MockRestServiceServer server;
	private DepartmentRepository departmentRepository;

	@Before
	public void before() {
		final RestTemplate restTemplate = new RestTemplate();
		departmentRepository = new DepartmentRepository(restTemplate, HOST, DEPARTMENT_BY_ID_URL,ALL_DEPARTMENT_URL);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void testShouldGetDepartmentById() {
		server.expect(once(), requestTo("http://host/eis/departments?id=1")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(new Resources().getFileContents("departmentResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));

		final List<Department> departments = departmentRepository.getById(1L);
		server.verify();
		assertEquals(1, departments.size());

	}
	
	@Test
	public void testShouldGetAllDepartments() {
		server.expect(once(), requestTo("http://host/eis/departments")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(new Resources().getFileContents("departmentResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));

		final List<Department> departments = departmentRepository.getAll();
		server.verify();
		assertEquals(1, departments.size());

	}

}

