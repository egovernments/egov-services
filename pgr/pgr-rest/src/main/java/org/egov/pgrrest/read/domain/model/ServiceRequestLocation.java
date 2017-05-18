package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Value
@Builder
public class ServiceRequestLocation {
    private Coordinates coordinates;
    private String crossHierarchyId;
    private String locationId;

    public boolean isRawLocationAbsent() {
        return isCoordinatesAbsent() && isHierarchyAbsent();
    }

    public boolean isLocationIdAbsent() { return isEmpty(locationId); }

    public boolean isCoordinatesAbsent() {
        return coordinates != null && coordinates.isAbsent();
    }

    public boolean isHierarchyAbsent() {
        return isEmpty(crossHierarchyId);
    }
}

