package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryServiceResponse {

    private String locationId;

    public String getLocationId() {
        return locationId;
    }
}
