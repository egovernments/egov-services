package org.egov.pgr.location.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.pgr.location.contract.BoundaryResponse;
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
    private static final String CREATE_URL = "/boundary/latitude={latitude}&longitude={longitude}";
    private MockRestServiceServer server;
    private BoundaryRepository boundaryRepository;

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        boundaryRepository = new BoundaryRepository(restTemplate, HOST, CREATE_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_fetch_location_for_given_coordinates() {
        server.expect(once(), requestTo("http://host/boundary/latitude=1.11&longitude=2.22"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(new Resources().getFileContents("successBoundaryResponse.json"),
                                MediaType.APPLICATION_JSON_UTF8));

        final BoundaryResponse boundary = boundaryRepository.findBoundary("1.11", "2.22","ap.public");

        server.verify();
        assertEquals(Long.valueOf(1), boundary.getId());
        assertEquals("foo", boundary.getName());

    }



}

