package org.egov.lams.repository;

import org.egov.lams.Resources;
import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class PositionRestRepositoryTest {

    public static final String HOST_NAME = "http://hr-employee:8080/";
    public static final String PATH_VARIABLE = "{employeeId}";
    public static final String SEARCH_PATH = "hr-employee/employees/{employeeId}/positions/_search";
    private MockRestServiceServer server;
    private  PositionRestRepository positionRestRepository;

    @Before
    public void before(){
        final RestTemplate restTemplate = new RestTemplate();
        positionRestRepository = new PositionRestRepository(restTemplate, HOST_NAME,
                SEARCH_PATH, PATH_VARIABLE);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_fetch_positions_of_employees(){
        server.expect(once(),
                requestTo("http://hr-employee:8080/hr-employee/employees/1/positions/_search?tenantId=ap.kurnool"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(new Resources().getFileContents("positionResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        PositionResponse positionResponse = positionRestRepository.getPositions("1","ap.kurnool", new RequestInfoWrapper());
        server.verify();
        assertTrue(null != positionResponse);
        assertEquals(Long.valueOf(1l), positionResponse.getPosition().get(0).getId());
    }
}