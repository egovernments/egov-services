package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.PositionHierarchyResponse;
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

public class PositionHierarchyRepositoryTest {

    private static final String HOST = "http://host";
    private static final String POSITIONHIERARCHIES_BY_ID_URL = "/escalation-hierarchy/v1/_searchfromPosition=1&serviceCode=complaintTypeCode&tenantId=default";

    private PositionHierarchyRepository positionHierarchyRepository;
    private MockRestServiceServer server;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        positionHierarchyRepository = new PositionHierarchyRepository(restTemplate, HOST, POSITIONHIERARCHIES_BY_ID_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_positionhierarchies_by_id() {
        server.expect(once(), requestTo(
            "http://host/escalation-hierarchy/v1/_searchfromPosition=1&serviceCode=complaintTypeCode&tenantId=default"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(new Resources().getFileContents("positionHierarchyResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        final PositionHierarchyResponse positionResponse = positionHierarchyRepository
            .getByPositionByComplaintTypeAndFromPosition(1l, "complaintTypeCode", "default");
        server.verify();
        assertEquals(1, positionResponse.getEscalationHierarchies().size());
    }

}
