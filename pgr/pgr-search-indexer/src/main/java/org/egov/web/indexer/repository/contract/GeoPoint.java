package org.egov.web.indexer.repository.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeoPoint {
    private double lat;
    private double lon;

    @Override
    public String toString() {
        return String.format("%s,%s", lat, lon);
    }
}


