package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.Resources;
import org.egov.pgr.employee.enrichment.model.Position;
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
        String positionHost = "http://hr-masters";
        String designationUrl = "/positions/_search?id=1&tenantId=default";
        positionRepository = new PositionRepository(restTemplate, positionHost, designationUrl);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_position_for_given_assignee() throws Exception {
        String tenantId = "default";
        Long assigneeId = 1L;
        String expectedUrl = "http://hr-masters/positions/_search?id=1&tenantId=default";
        server.expect(once(), requestTo(expectedUrl)).andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(resources.getFileContents("positionResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        Position position = positionRepository.getDesignationIdForAssignee(tenantId, assigneeId);
        server.verify();
        assertEquals(new Position("2", "2"), position);
    }

}