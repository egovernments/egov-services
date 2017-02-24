package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.egov.workflow.domain.model.Employee;
import org.egov.workflow.domain.model.EmployeeRes;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    private static final String HOST = "http://host";
    private static final String EMPLOYEES_BY_USERID_URL = "/eis/employee?tenantId=ap.public&userId=1";
    private static final String EMPLOYEES_BY_POSITIONID_URL = "/eis/employee?tenantId=ap.public&positionId=1";
    private static final String EMPLOYEES_BY_ROLENAME_URL = "/eis/employee?tenantId=ap.public&roleName='Grievance/Officer'";

    private MockRestServiceServer server;
    private EmployeeServiceImpl employeeServiceImpl;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        employeeServiceImpl = new EmployeeServiceImpl(restTemplate, HOST, EMPLOYEES_BY_USERID_URL, EMPLOYEES_BY_POSITIONID_URL,
                EMPLOYEES_BY_ROLENAME_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_employees_by_userid() {
        server.expect(once(), requestTo("http://host/eis/employee?tenantId=ap.public&userId=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("employeeResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final EmployeeRes employeeRes = employeeServiceImpl.getEmployeeForUserId(1l);
        server.verify();
        assertEquals(1, employeeRes.getEmployees().size());

    }

    @Test
    public void test_should_return_employees_by_positionid() {
        server.expect(once(), requestTo("http://host/eis/employee?tenantId=ap.public&positionId=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("employeeResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final EmployeeRes employeeRes = employeeServiceImpl.getEmployeeForPosition(1l, new LocalDate());
        server.verify();
        assertEquals(1, employeeRes.getEmployees().size());
    }

    @Test
    public void test_should_return_employees_by_rolename() {
        server.expect(once(), requestTo("http://host/eis/employee?tenantId=ap.public&roleName='Grievance/Officer'"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("employeeResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        List<Employee> employees = employeeServiceImpl.getByRoleName("Grievance Officer");
        server.verify();
        assertEquals(1, employees.size());
    }
}
