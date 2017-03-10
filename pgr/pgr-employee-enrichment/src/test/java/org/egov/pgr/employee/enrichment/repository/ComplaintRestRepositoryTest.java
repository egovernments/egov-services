package org.egov.pgr.employee.enrichment.repository;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.pgr.employee.enrichment.Resources;
import org.egov.pgr.employee.enrichment.repository.contract.ServiceRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class ComplaintRestRepositoryTest {

	private static final String HOST = "http://host";
	private static final String COMPLAINT_BY_CRN = "/pgr/seva?jurisdiction_id={tenantId}&service_request_id="
			+ "00015-2016-AP";

	private ComplaintRestRepository complaintRestRepositoryRepository;
	private MockRestServiceServer server;

	@Before
	public void before() {
		final RestTemplate restTemplate = new RestTemplate();
		complaintRestRepositoryRepository = new ComplaintRestRepository(restTemplate, HOST, COMPLAINT_BY_CRN);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void testShouldGetComplaintByCrnNo() {
		server.expect(once(),
				requestTo("http://host/pgr/seva?jurisdiction_id=" + 1L + "&service_request_id=" + "00015-2016-AP"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(new Resources().getFileContents("complaintSearchResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));
		final ServiceRequest sevaRequest = complaintRestRepositoryRepository.getComplaintByCrn(1L, "00015-2016-AP");
		server.verify();
		assertTrue(sevaRequest != null);
	}

}
