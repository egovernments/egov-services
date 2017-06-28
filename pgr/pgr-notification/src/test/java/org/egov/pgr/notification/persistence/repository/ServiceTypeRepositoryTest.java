package org.egov.pgr.notification.persistence.repository;

import org.egov.Resources;
import org.egov.pgr.notification.domain.model.ServiceType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ServiceTypeRepositoryTest {
    private static final String HOST = "http://host";
    private static final String COMPLAINT_TYPE_URL = "/pgr/services/v1/{code}/_search?tenantId={tenantId}";

    private ServiceTypeRepository serviceTypeRepository;
    private MockRestServiceServer server;
    private final Resources resources = new Resources();

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        serviceTypeRepository = new ServiceTypeRepository(restTemplate, HOST, COMPLAINT_TYPE_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_complaint_type_id_for_given_complaint_code() {
        server.expect(once(),
            requestTo("http://host/pgr/services/v1/serviceTypeCode/_search?tenantId=ap.kurnool"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(resources.getFileContents("serviceTypeResponse.json"),
                MediaType.APPLICATION_JSON_UTF8));

        final ServiceType serviceType = serviceTypeRepository
            .getServiceTypeByCode("serviceTypeCode", "ap.kurnool");

        server.verify();
        assertNotNull(serviceType);
        assertEquals("servicename", serviceType.getName());
        assertEquals(Arrays.asList("keyword1", "keyword2"), serviceType.getKeywords());
    }
}