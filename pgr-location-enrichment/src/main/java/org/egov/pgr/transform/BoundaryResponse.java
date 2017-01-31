package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryResponse {

    private Long id;

    public Long getId() {
        return id;
    }
}
