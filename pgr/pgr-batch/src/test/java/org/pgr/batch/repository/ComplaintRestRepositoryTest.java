package org.pgr.batch.repository;

import org.egov.common.contract.request.RequestInfo;
import org.junit.Before;
import org.junit.Test;
import org.pgr.batch.repository.contract.ServiceResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ComplaintRestRepositoryTest {

    private static final String HOST = "http://host";
    private static final String COMPLAINT_BY_CRN = "/pgr/seva?tenantId=tenantId";

    private ComplaintRestRepository complaintRestRepositoryRepository;
    private MockRestServiceServer server;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        complaintRestRepositoryRepository = new ComplaintRestRepository(restTemplate, HOST, COMPLAINT_BY_CRN);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetComplaintsEligibleForEscalation() {
        server.expect(once(),
                requestTo("http://host/pgr/seva?tenantId=tenantId"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(new Resources().getFileContents("complaintSearchResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));
        final ServiceResponse response = complaintRestRepositoryRepository.getComplaintsEligibleForEscalation("default", 1L);
        server.verify();
        assertTrue(response.getServiceRequests() != null);
    }

    private RequestInfo getRequestInfo() {
        return RequestInfo.builder().build();
    }
}
