package org.egov.pgr.location.repository;

import org.egov.pgr.location.contract.CrossHierarchyResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class CrossHierarchyRepositoryTest {

    private static final String HOST = "http://host/";
    private static final String CREATE_URL = "egov-location/crosshierarchys/_search";
    private MockRestServiceServer server;
    private CrossHierarchyRepository crossHierarchyRepository;

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        crossHierarchyRepository = new CrossHierarchyRepository(HOST, CREATE_URL, restTemplate);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_fetch_hierarchy_for_given_id() {
        server.expect(once(), requestTo("http://host/egov-location/crosshierarchys/_search"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withSuccess(new Resources().getFileContents("successHierarchyResponse.json"),
                    MediaType.APPLICATION_JSON_UTF8));

        final CrossHierarchyResponse response = crossHierarchyRepository.getCrossHierarchy("5", "tenantId");

        server.verify();
        assertEquals("1", response.getLocationId());
        assertEquals("parent", response.getLocationName());
        assertEquals("2", response.getChildLocationId());

    }


}