package org.egov.web.indexer.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.web.indexer.contract.ServiceType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class ServiceTypeRepositoryTest {
	private static final String HOST = "http://host/";
	private MockRestServiceServer server;
	private ServiceTypeRepository complaintTypeRepository;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		complaintTypeRepository = new ServiceTypeRepository(restTemplate, HOST);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_complainttype_for_given_code() throws Exception {
		server.expect(once(), requestTo("http://host/pgr/services/v1/AOS/_search?tenantId=ap.public")).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(new Resources().getFileContents("successComplaintTypeResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));

		final ServiceType complaintType = complaintTypeRepository.fetchServiceTypeByCode("AOS","ap.public");

		server.verify();
		assertEquals("10", complaintType.getSlaHours().toString());

	}
}
