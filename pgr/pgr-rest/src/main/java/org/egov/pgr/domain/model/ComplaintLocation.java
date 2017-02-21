package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Value
public class ComplaintLocation {
    private Coordinates coordinates;
    private String crossHierarchyId;
    private String locationId;

    public boolean isAbsent() {
        return isLocationIdAbsent() && isCoordinatesAbsent() && isHierarchyAbsent();
    }

    private boolean isLocationIdAbsent() { return isEmpty(locationId); }

    private boolean isCoordinatesAbsent() {
        return coordinates != null && coordinates.isAbsent();
    }

    private boolean isHierarchyAbsent() {
        return isEmpty(crossHierarchyId);
    }
}

