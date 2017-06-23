package org.egov.pgr.location.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CrossHierarchyResponse {
	private BoundaryResponse parent;
	private BoundaryResponse child;

    public String getLocationId() {
        return parent.getId().toString();
    }

    public String getLocationName() {
        return parent.getName();
    }

    public String getChildLocationId() {
        return child.getId().toString();
    }
}
