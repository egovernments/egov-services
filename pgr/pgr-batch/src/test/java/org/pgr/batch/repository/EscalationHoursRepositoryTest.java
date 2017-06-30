package org.pgr.batch.repository;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class EscalationHoursRepositoryTest {
    private MockRestServiceServer server;
    private EscalationHoursRepository escalationHoursRepository;
    private Resources resources = new Resources();

    @Before
    public void setUp() throws Exception {
        final RestTemplate restTemplate = new RestTemplate();
        String positionHost = "http://host/";
        String designationUrl = "escalation-hours/v1/_search?tenantId={tenantId}&complaintTypeId={complaintTypeId}&designationId={designationId}";
        escalationHoursRepository = new EscalationHoursRepository(restTemplate, positionHost, designationUrl);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_return_escalation_hours_for_given_search_criteria() throws Exception {
        String tenantId = "tenantId";
        final String complaintTypeId = "1";
        final String designationId1 = "2";
        String expectedUrl = "http://host/escalation-hours/v1/_search?tenantId=tenantId&complaintTypeId=1&designationId=2";
        server.expect(once(), requestTo(expectedUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(resources.getFileContents("escalationHoursRequest.json")))
                .andRespond(withSuccess(resources.getFileContents("escalationHoursResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));


        final int escalationHours = escalationHoursRepository
                .getEscalationHours(tenantId, complaintTypeId, designationId1);
        server.verify();
        assertEquals(3, escalationHours);
    }
}