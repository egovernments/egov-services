package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.CityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CityRepository {

	private final String cityServiceHost;
	private RestTemplate restTemplate;

	public CityRepository(RestTemplate restTemplate, @Value("${egov.services.boundary.host}") String cityServiceHost) {
		this.restTemplate = restTemplate;
		this.cityServiceHost = cityServiceHost;
	}

	public City fetchCityById(Long id) {
		String url = this.cityServiceHost + "/v1/location/city/getCitybyCityRequest?city.id={id}";
		return getBoundaryServiceResponse(url, id).getCity();
	}

	private CityResponse getBoundaryServiceResponse(final String url, Long id) {
		return restTemplate.getForObject(url, CityResponse.class, id);
	}

}