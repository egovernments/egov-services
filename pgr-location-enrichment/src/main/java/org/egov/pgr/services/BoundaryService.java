package org.egov.pgr.services;

import org.egov.pgr.model.RequestInfo;

public interface BoundaryService {

    Long fetchBoundaryByLatLng(RequestInfo requestInfo, Double lat, Double lng);

    Long fetchBoundaryByCrossHierarchy(RequestInfo requestInfo, String crossHierarchyId);

}
