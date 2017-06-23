package org.egov.pgr.notification.persistence.repository;

import org.egov.Resources;
import org.egov.pgr.notification.domain.model.Tenant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
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
        String host = "http://host/";
        String url = "tenant/v1/tenant?code={code}";
        RestTemplate restTemplate = new RestTemplate();
        tenantRepository = new TenantRepository(restTemplate, host, url);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_fetch_tenant_for_given_id() throws Exception {
        server.expect(once(),
            requestTo("http://host/tenant/v1/tenant?code=tenantId"))
            .andExpect(method(HttpMethod.POST))

            .andRespond(withSuccess(resources.getFileContents("successTenantResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        final Tenant tenant = tenantRepository.fetchTenantById("tenantId");

        server.verify();
        assertNotNull(tenant);
        assertEquals("cityname", tenant.getName());
    }
}