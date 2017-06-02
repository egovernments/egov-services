package org.pgr.batch.repository;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pgr.batch.repository.contract.SearchTenantResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
public class TenantRepositoryTest {

    private MockRestServiceServer server;
    private TenantRepository tenantRepository;
    private Resources resources = new Resources();

    @Before
    public void setUp() throws Exception {
        final RestTemplate restTemplate = new RestTemplate();
        String tenantHost = "http://tenant";
        String tenantUrl = "/v1/tenant/_search";
        tenantRepository = new TenantRepository(restTemplate, tenantHost, tenantUrl);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_to_return_all_tenants(){
        String expectedUrl = "http://tenant/v1/tenant/_search";
        server.expect(once(), requestTo(expectedUrl)).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(resources.getFileContents("tenantResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        SearchTenantResponse response = tenantRepository.getAllTenants();
        server.verify();
        assertEquals( "default", response.getTenant().get(0).getCode());
    }
}