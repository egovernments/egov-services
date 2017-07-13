package org.egov.collection.indexer.repository;

import org.egov.collection.indexer.Resources.Resources;
import org.egov.collection.indexer.contract.City;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class TenantRepositoryTest {


    private MockRestServiceServer server;
    private TenantRepository tenantRepository;
    private final Resources resources = new Resources();

    @Before
    public void before() {
        String HOST = "http://host/";
        RestTemplate restTemplate = new RestTemplate();
        tenantRepository = new TenantRepository(restTemplate, HOST);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_fetch_tenant_for_given_id() throws Exception {
        server.expect(once(),
            requestTo("http://host/tenant/v1/tenant/_search?code=default"))
            .andExpect(method(HttpMethod.POST))

            .andRespond(withSuccess(resources.getFileContents("successTenantResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        final City city = tenantRepository.fetchTenantByCode("default");

        server.verify();
        assertEquals("default", city.getName());
        assertEquals("default", city.getCode());
    }

    @Test
    public void test_should_fail_when_tenant_id_null() throws Exception {
        server.expect(once(),
            requestTo("http://host/tenant/v1/tenant/_search?code=null"))
            .andExpect(method(HttpMethod.POST))

            .andRespond(withSuccess(resources.getFileContents("failureTenantResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        final City city = tenantRepository.fetchTenantByCode(null);

        server.verify();
        assertNull(city);
    }
}