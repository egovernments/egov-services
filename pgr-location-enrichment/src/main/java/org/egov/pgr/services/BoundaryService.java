package org.egov.pgr.services;

public interface BoundaryService {

    String fetchBoundaryByLatLng(Double lat, Double lng);

    String fetchBoundaryByCrossHierarchy(String crossHierarchyId);

}
