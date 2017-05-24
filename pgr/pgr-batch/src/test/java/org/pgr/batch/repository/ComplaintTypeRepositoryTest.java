package org.pgr.batch.repository;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ComplaintTypeRepositoryTest {
    private static final String HOST = "http://host";
    private static final String COMPLAINT_TYPE_URL = "/pgr/services/{code}/_search?tenantId={tenantId}";

    private ComplaintTypeRepository complaintTypeRepository;
    private MockRestServiceServer server;
    private final Resources resources = new Resources();

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        complaintTypeRepository = new ComplaintTypeRepository(restTemplate, HOST, COMPLAINT_TYPE_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_complaint_type_id_for_given_complaint_code() {
        server.expect(once(),
                requestTo("http://host/pgr/services/complaintTypeCode/_search?tenantId=tenantId"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(resources.getFileContents("complaintTypeResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final String complaintTypeId = complaintTypeRepository
                .getComplaintTypeId("complaintTypeCode", "tenantId");

        server.verify();
        assertEquals("5", complaintTypeId);
    }
}