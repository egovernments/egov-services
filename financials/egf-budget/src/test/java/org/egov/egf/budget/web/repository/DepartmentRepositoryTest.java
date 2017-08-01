package org.egov.egf.budget.web.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.egov.egf.budget.domain.service.DateFactory;
import org.egov.egf.budget.utils.RequestJsonReader;
import org.egov.egf.budget.web.contract.DepartmentRes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentRepositoryTest {

	private static final String HOST = "http://host";

	private static final String DEPT_BY_ID_URL = "/departments/findById";

	private MockRestServiceServer server;

	private DepartmentRepository departmentRepository;

	@Mock
	private DateFactory dateFactory;

	private RequestJsonReader resources = new RequestJsonReader();

	@Before
	public void setup() {
		final RestTemplate restTemplate = new RestTemplate();
		departmentRepository = new DepartmentRepository(restTemplate, HOST, DEPT_BY_ID_URL, dateFactory);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_get_by_id() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

		Date now = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2017 00:00:00");
		server.expect(once(), requestTo("http://host/departments/findById")).andExpect(method(HttpMethod.POST))
				.andExpect(content().string(resources.getFileContents("department/search_dept_by_id_request.json")))
				.andRespond(withSuccess(resources.getFileContents("department/search_dept_by_id.json"),
						MediaType.APPLICATION_JSON_UTF8));

		when(dateFactory.create()).thenReturn(now);

		final DepartmentRes response = departmentRepository.getDepartmentById("departmentId", "tenantId");
		server.verify();

		assertEquals(1, response.getDepartment().size());

	}
}
