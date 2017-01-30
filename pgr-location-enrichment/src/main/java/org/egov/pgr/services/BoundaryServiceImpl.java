package org.egov.pgr.services;

import org.egov.pgr.transform.BoundaryServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryServiceImpl implements BoundaryService {

    @Override
    public String fetchBoundaryByLatLng(Double lat, Double lng) {
        String url = "";
//        return getBoundaryServiceResponse(url).getLocationId();
        return "22";
    }

    @Override
    public String fetchBoundaryByCrossHierarchy(String crossHierarchyId) {
        String url = "";
//        return getBoundaryServiceResponse(url).getLocationId();
        return "22";
    }

    private BoundaryServiceResponse getBoundaryServiceResponse(final String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, BoundaryServiceResponse.class);
    }

}
