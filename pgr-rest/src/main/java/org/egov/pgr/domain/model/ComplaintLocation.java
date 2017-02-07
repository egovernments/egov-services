package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Value
public class ComplaintLocation {
    private Coordinates coordinates;
    private String crossHierarchyId;

    public boolean isAbsent() {
        return isCoordinatesAbsent() && isHierarchyAbsent();
    }

    private boolean isCoordinatesAbsent() {
        return coordinates != null && coordinates.isAbsent();
    }

    private boolean isHierarchyAbsent() {
        return isEmpty(crossHierarchyId);
    }
}

