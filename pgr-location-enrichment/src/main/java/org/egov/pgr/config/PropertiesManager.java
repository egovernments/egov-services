package org.egov.pgr.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

    @Value("${kafka.topics.pgr.boundary_enriched.name}")
    @Getter
    private String locationEnrichedTopicName;

    @Value("${egov.services.boundary.host}")
    private String boundaryServiceHost;

    @Value("${egov.services.boundary.context.fetch_by_lat_lng}")
    private String fetchByLatLng;

    public String getFetchBoundaryByLatLngUrl() {
        return boundaryServiceHost + fetchByLatLng;
    }

}
