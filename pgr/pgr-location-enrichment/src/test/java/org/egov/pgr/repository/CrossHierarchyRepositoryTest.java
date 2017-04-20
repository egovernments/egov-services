package org.egov.pgr.repository;

import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.CrossHierarchyResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class CrossHierarchyRepositoryTest {

    private static final String HOST = "http://host";
    private static final String CREATE_URL = "location/crosshierarchys?id={crossHierarchyId}";
    private MockRestServiceServer server;
    private CrossHierarchyRepository crossHierarchyRepository;

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        crossHierarchyRepository = new CrossHierarchyRepository(HOST, CREATE_URL, restTemplate);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_fetch_hierarchy_for_given_id() {
        server.expect(once(), requestTo("http://hostlocation/crosshierarchys?id=5"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(new Resources().getFileContents("successHierarchyResponse.json"),
                                MediaType.APPLICATION_JSON_UTF8));

        final CrossHierarchyResponse response = crossHierarchyRepository.getCrossHierarchy("5","tenantId");

        server.verify();
        assertEquals("1", response.getLocationId());
        assertEquals("parent", response.getLocationName());
        assertEquals("2", response.getChildLocationId());

    }





}