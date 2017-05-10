package org.egov.web.indexer.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.web.indexer.contract.Boundary;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class BoundaryRepositoryTest {

	private static final String HOST = "http://host/";
	private MockRestServiceServer server;
	private BoundaryRepository boundaryRepository;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		boundaryRepository = new BoundaryRepository(restTemplate, HOST);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_boundary_for_given_id() throws Exception {
		server.expect(once(), requestTo("http://host/egov-location/boundarys?boundary.id=1&boundary.tenantId=ap.public"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(new Resources().getFileContents("successBoundaryResponse.json"),
						MediaType.APPLICATION_JSON_UTF8));

		final Boundary boundary = boundaryRepository.fetchBoundaryById(1L,"ap.public");

		server.verify();
		assertEquals(Long.valueOf(1), boundary.getId());
		assertEquals("Kurnool", boundary.getName());

	}

}
