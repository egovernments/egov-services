package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Coordinates {
    private Double latitude;
    private Double longitude;

    public boolean isAbsent() {
        return (latitude == null || latitude <= 0) && (longitude == null || longitude <= 0);
    }
}
