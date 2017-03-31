package org.egov.pgr.write.repository;

import org.egov.pgr.Resources;
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
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class PositionRepositoryTest {
    @Mock
    private MockRestServiceServer server;
    private PositionRepository positionRepository;

    @Before
    public void setUp() throws Exception {
        String host = "http://position/";
        String urlSuffix = "assignee/{tenantId}/{assigneeId}";
        final RestTemplate restTemplate = new RestTemplate();
        positionRepository = new PositionRepository(restTemplate, host, urlSuffix);
        server = MockRestServiceServer.bindTo(restTemplate).build();
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