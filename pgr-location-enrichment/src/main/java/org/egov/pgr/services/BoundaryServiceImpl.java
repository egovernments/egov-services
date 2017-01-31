package org.egov.pgr.services;

import org.egov.pgr.model.BoundaryServiceRequest;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.transform.BoundaryServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryServiceImpl implements BoundaryService {

    @Override
    public Long fetchBoundaryByLatLng(RequestInfo requestInfo, Double lat, Double lng) {
        String url = "http://www.mocky.io/v2/589037550f00006f16a3effa";
        BoundaryServiceRequest request = new BoundaryServiceRequest(requestInfo, String.valueOf(lat), String.valueOf(lng));
        return getBoundaryServiceResponse(url, request).getBoundary().get(0).getId();
    }

    @Override
    public Long fetchBoundaryByCrossHierarchy(RequestInfo requestInfo, String crossHierarchyId) {
        String url = "http://www.mocky.io/v2/589037550f00006f16a3effa";
        BoundaryServiceRequest request = new BoundaryServiceRequest(requestInfo, crossHierarchyId);
        return getBoundaryServiceResponse(url, request).getBoundary().get(0).getId();
    }

    private BoundaryServiceResponse getBoundaryServiceResponse(final String url, final BoundaryServiceRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, req, BoundaryServiceResponse.class);
    }

}
