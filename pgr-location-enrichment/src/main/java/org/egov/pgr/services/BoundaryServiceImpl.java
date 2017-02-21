package org.egov.pgr.services;

import org.egov.pgr.config.PropertiesManager;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.BoundaryServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryServiceImpl implements BoundaryService {

    private RestTemplate restTemplate;
    private PropertiesManager propertiesManager;

    @Autowired
    public BoundaryServiceImpl(RestTemplate restTemplate, PropertiesManager propertiesManager) {
        this.restTemplate = restTemplate;
        this.propertiesManager = propertiesManager;
    }

    @Override
    public BoundaryResponse fetchBoundaryByLatLng(RequestInfo requestInfo, Double lat, Double lng) {
        String url = propertiesManager.getFetchBoundaryByLatLngUrl();
        return getBoundaryServiceResponse(url, String.valueOf(lat), String.valueOf(lng)).getBoundary().get(0);
    }

    private BoundaryServiceResponse getBoundaryServiceResponse(final String url, String... args) {
        return restTemplate.getForObject(url, BoundaryServiceResponse.class, args);
    }

}
