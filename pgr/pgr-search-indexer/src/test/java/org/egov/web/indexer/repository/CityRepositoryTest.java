package org.egov.web.indexer.repository;

import static org.junit.Assert.assertEquals;

import org.egov.web.indexer.contract.City;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class CityRepositoryTest {

	private CityRepository cityRepository;
	private MockRestServiceServer server;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		cityRepository = new CityRepository();
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_city_for_given_id() throws Exception {
		City city = cityRepository.fetchCityById(1L);
		server.verify();
		assertEquals("Kurnool", city.getName());
		assertEquals("KC", city.getCode());
	}

}
