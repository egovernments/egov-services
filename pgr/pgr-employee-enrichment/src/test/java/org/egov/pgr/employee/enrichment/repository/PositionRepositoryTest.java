package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.Resources;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class PositionRepositoryTest {

    private MockRestServiceServer server;
    private PositionRepository positionRepository;
    private Resources resources = new Resources();

    @Before
    public void setUp() throws Exception {
        final RestTemplate restTemplate = new RestTemplate();
        String positionHost = "http://position";
        String designationUrl = "/eis/position?tenantId={tenant_id}&id={assignee}";
        positionRepository = new PositionRepository(restTemplate, positionHost, designationUrl);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetDesignationByAssignee() throws Exception {
        String tenantId = "ap.hyd";
        Long assigneeId = 1L;
        String expectedUrl = "http://position/eis/position?tenantId=ap.hyd&id=1";
        server.expect(once(), requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(resources.getFileContents("positionResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        String designationId = positionRepository.getDesignationIdForAssignee(tenantId, assigneeId);
        server.verify();
        assertEquals("12", designationId);
    }

}