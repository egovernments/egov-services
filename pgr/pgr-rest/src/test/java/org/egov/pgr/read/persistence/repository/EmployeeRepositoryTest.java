package org.egov.pgr.read.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Set;

import org.egov.pgr.common.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class EmployeeRepositoryTest {

    private static final String HOST = "http://host";
    private static final String ROLES_BY_USERID_URL = "/eis/employee?userId=1&tenantId=ap.public";

    private EmployeeRepository employeeRepository;
    private MockRestServiceServer server;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        employeeRepository = new EmployeeRepository(restTemplate, HOST, ROLES_BY_USERID_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_roles_by_userid() {
        server.expect(once(), requestTo("http://host/eis/employee?userId=1&tenantId=ap.public"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("roleResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));
        final Set<Role> roles = employeeRepository.getRolesByUserId(1L, "ap.public");
        server.verify();
        assertEquals(2, roles.size());
    }

}
