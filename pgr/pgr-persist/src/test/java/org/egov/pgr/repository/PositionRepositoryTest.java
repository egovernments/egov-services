package org.egov.pgr.repository;

import org.egov.pgr.config.PersistenceProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class PositionRepositoryTest {

    @Mock
    private PersistenceProperties persistenceProperties;
    private MockRestServiceServer server;
    private String url = "http://position/assignee/{tenantId}/{assigneeId}";
    private PositionRepository positionRepository;

    @Before
    public void setUp() throws Exception {
        when(persistenceProperties.getPositionServiceEndpoint()).thenReturn(url);
        final RestTemplate restTemplate = new RestTemplate();
        positionRepository = new PositionRepository(restTemplate, persistenceProperties);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetDesignationByAssignee() throws Exception {
        String tenantId = "ap.hyd";
        Long assigneeId=12L;
        String expectedUrl = "http://position/assignee/"+tenantId+"/"+assigneeId;
        server.expect(once(), requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("positionResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        Long designationId = positionRepository.designationIdForAssignee(tenantId, assigneeId);
        server.verify();
        assertEquals(Long.valueOf(12), designationId);
    }

    @Test
    public void testShouldGetDepartmentByAssignee() throws Exception {
        String tenantId = "ap.hyd";
        Long assigneeId=12L;
        String expectedUrl = "http://position/assignee/"+tenantId+"/"+assigneeId;
        server.expect(once(), requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("positionResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        Long designationId = positionRepository.departmentIdForAssignee(tenantId, assigneeId);
        server.verify();
        assertEquals(Long.valueOf(18), designationId);
    }
}