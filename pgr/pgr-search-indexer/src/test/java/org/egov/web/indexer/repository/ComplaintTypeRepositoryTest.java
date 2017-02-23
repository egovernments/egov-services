package org.egov.web.indexer.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.web.indexer.contract.ComplaintType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class ComplaintTypeRepositoryTest {
	private static final String HOST = "http://host/";
	private MockRestServiceServer server;
	private ComplaintTypeRepository complaintTypeRepository;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		complaintTypeRepository = new ComplaintTypeRepository(restTemplate, HOST);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_complainttype_for_given_code() throws Exception {
		server.expect(once(), requestTo("http://host/pgr/services/AOS?tenantId=")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(new Resources().getFileContents("successComplaintTypeResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));

		final ComplaintType complaintType = complaintTypeRepository.fetchComplaintTypeByCode("AOS");

		server.verify();
		assertEquals("10", complaintType.getSlaHours().toString());

	}
}
