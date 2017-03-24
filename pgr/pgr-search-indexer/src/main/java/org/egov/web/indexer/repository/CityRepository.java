package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.CityRequest;
import org.egov.web.indexer.contract.CityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
        String url = this.cityServiceHost + "v1/location/city/getCitybyCityRequest";
        return getCityServiceResponse(url, id).getBody().getCity();
    }

    private ResponseEntity<CityResponse> getCityServiceResponse(final String url, Long id) {
        CityRequest cityReq = new CityRequest();
        City city = new City();
        city.setId(id.toString());
        cityReq.setCity(city);
        return restTemplate.postForEntity(url, cityReq, CityResponse.class);
    }

}