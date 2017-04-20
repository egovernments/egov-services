package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.PositionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class PositionRepositoryTest {
    
    private static final String HOST = "http://host";
    private static final String POSITIONS_BY_ID_URL = "/eis/positions?position.id=1";
    private static final String POSITIONS_BY_EMPCODE_URL = "/eis/employee/102012/positions";
    
    private PositionRepository positionRepository;
    private MockRestServiceServer server;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        positionRepository = new PositionRepository(restTemplate, HOST, POSITIONS_BY_ID_URL, POSITIONS_BY_EMPCODE_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_positions_by_id() {
        server.expect(once(), requestTo("http://host/eis/positions?position.id=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("positionResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        PositionResponse positionResponse = positionRepository.getById(1l,"tenantId");
        server.verify();
        assertEquals("position1", positionResponse.getName());
        assertEquals(1, positionResponse.getId().intValue());
    }
    
    @Test
    public void test_return_positions_by_employee_code() {
        server.expect(once(), requestTo("http://host/eis/employee/102012/positions"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(new Resources().getFileContents("positionResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));
        
        List<PositionResponse> positionResponse = positionRepository.getByEmployeeCode("102012");
        server.verify();
        assertEquals(1, positionResponse.size());
        
    }
}
