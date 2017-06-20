package org.egov.pgr.common.repository;

import org.egov.pgr.common.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationRepository {

    private final RestTemplate restTemplate;
    private final String url;

    private final static String BOUNDARY_BY_ID =
        "egov-location/boundarys?Boundary.id={id}&Boundary.tenantId={tenantId}";

    public LocationRepository(RestTemplate restTemplate,
                              @Value("${location.host}") final String locationServiceHostname) {
        this.restTemplate = restTemplate;
        this.url = locationServiceHostname + BOUNDARY_BY_ID;
    }

    public Location getLocationById(String id, String tenantId) {
        final BoundaryResponse boundaryResponse = this
            .restTemplate.getForObject(this.url, BoundaryResponse.class, id, tenantId);
        return boundaryResponse.toDomain();
    }
}
