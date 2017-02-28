package org.egov.workflow.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.egov.workflow.web.contract.PositionHierarchyResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class PositionHierarchyRepositoryTest {

    private static final String HOST = "http://host";
    private static final String POSITIONHIERARCHIES_BY_ID_URL = "/eis/positionhierarchys?positionHierarchy.objectType.type=Complaint&positionHierarchy.objectSubType=complaintTypeCode&positionHierarchy.fromPosition.id=1";

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
                "http://host/eis/positionhierarchys?positionHierarchy.objectType.type=Complaint&positionHierarchy.objectSubType=complaintTypeCode&positionHierarchy.fromPosition.id=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("positionHierarchyResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final List<PositionHierarchyResponse> positionResponse = positionHierarchyRepository
                .getByObjectTypeObjectSubTypeAndFromPosition("Complaint", "complaintTypeCode", 1l);
        server.verify();
        assertEquals(1, positionResponse.size());
    }

}
