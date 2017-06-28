package org.egov.workflow.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.workflow.domain.service.Resources;
import org.egov.workflow.web.contract.ComplaintTypeResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintTypeRepositoryTest {

    private static final String HOST = "http://host";
    private static final String COMPLAINT_TYPES_BY_CODE_URL = "/pgr/services/v1/C001?tenantId=ap.public";

    private MockRestServiceServer server;

    private ComplaintTypeRepository complaintTypeRepository;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        complaintTypeRepository = new ComplaintTypeRepository(restTemplate, HOST, COMPLAINT_TYPES_BY_CODE_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_get_complainttype_by_code() {
        server.expect(once(), requestTo("http://host/pgr/services/v1/C001?tenantId=ap.public"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(new Resources().getFileContents("complaintTypeResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final ComplaintTypeResponse complaintResponseExpected = complaintTypeRepository.fetchComplaintTypeByCode("C001","ap.public");
        server.verify();
        assertEquals(2, complaintResponseExpected.getId().intValue());
        assertEquals("C001", complaintResponseExpected.getServiceCode());
    }

}
