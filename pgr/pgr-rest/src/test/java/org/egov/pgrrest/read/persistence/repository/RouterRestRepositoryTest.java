package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.RouterResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class RouterRestRepositoryTest {

    private static final String HOST = "http://host/";
    private static final String ROUTER_BY_HIERARCHT_TYPE = "workflow/router/v1/_search?tenantId={tenantId}&hierarchyType={hierarchyType}";
    public static final String ADMINISTRATION = "ADMINISTRATION";
    public static final String DEFAULT = "default";

    private MockRestServiceServer server;
    private RouterRestRepository routerRestRepository;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        routerRestRepository = new RouterRestRepository(restTemplate, HOST, ROUTER_BY_HIERARCHT_TYPE);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetComplaintByCrnNo() {
        server.expect(once(),
            requestTo("http://host/workflow/router/v1/_search?tenantId=default&hierarchyType=ADMINISTRATION"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(new Resources().getFileContents("routerResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));
        final RouterResponse sevaRequest = routerRestRepository.getRouter(DEFAULT, ADMINISTRATION);
        server.verify();
        assertTrue(sevaRequest != null);
        assertEquals(Long.valueOf(1l), sevaRequest.getRouterTypes().get(0).getId());
    }
}