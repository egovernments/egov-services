package org.egov.pgr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

    @Value("${kafka.topics.pgr.boundary_enriched.name}")
    private String locationEnrichedTopicName;

    public String getLocationEnrichedTopicName() {
        return locationEnrichedTopicName;
    }
}
