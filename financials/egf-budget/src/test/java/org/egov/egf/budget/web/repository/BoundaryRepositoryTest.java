package org.egov.egf.budget.web.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.egf.budget.utils.RequestJsonReader;
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

    private static final String HOST = "http://host/";

    private MockRestServiceServer server;

    private BoundaryRepository boundaryRepository;

    private final RequestJsonReader resources = new RequestJsonReader();

    @Before
    public void setup() {
        final RestTemplate restTemplate = new RestTemplate();
        boundaryRepository = new BoundaryRepository(restTemplate, HOST);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_get_by_id() throws Exception {

        server.expect(once(), requestTo("http://host/egov-location/boundarys?boundary.id=boundaryId&boundary.tenantId=tenantId"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(resources.getFileContents("boundary/search_boundary_by_id.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        boundaryRepository.getBoundaryById("boundaryId", "tenantId");

        server.verify();

        assertEquals(1, 1);

    }
}
