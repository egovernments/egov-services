package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.City;
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

public class CityRepositoryTest {

	private static final String HOST = "http://host/";
	private MockRestServiceServer server;
	private CityRepository cityRepository;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		cityRepository = new CityRepository(restTemplate, HOST);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_city_for_given_id() throws Exception {
		server.expect(once(), requestTo("http://host/v1/location/city/getCitybyCityRequest"))
				.andExpect(method(HttpMethod.POST)).andRespond(withSuccess(
						new Resources().getFileContents("successCityResponse.json"), MediaType.APPLICATION_JSON_UTF8));

		final City city = cityRepository.fetchCityById(1L);

		server.verify();
		assertEquals("Kurnool", city.getName());
		assertEquals("KC", city.getCode());

	}

}