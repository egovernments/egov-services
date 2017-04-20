package org.egov.workflow.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.workflow.domain.service.Resources;
import org.egov.workflow.web.contract.BoundaryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryRepositoryTest {
    
    private static final String HOST = "http://host";
    private static final String BOUNDARIES_BY_ID_URL = "/v1/location/boundarys?boundary.id=1";
    
    private MockRestServiceServer server;
    private BoundaryRepository boundaryRepository;
    
    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        boundaryRepository = new BoundaryRepository(restTemplate, HOST, BOUNDARIES_BY_ID_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_boundaries_by_id() {
        server.expect(once(), requestTo("http://host/v1/location/boundarys?boundary.id=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new Resources().getFileContents("boundaryResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final BoundaryResponse boundaryResponse = boundaryRepository.fetchBoundaryById(1l, "tenantId");
        server.verify();
        assertEquals(2, boundaryResponse.getId().intValue());
        assertEquals("Srinivas Nagar", boundaryResponse.getName());

    }

}
