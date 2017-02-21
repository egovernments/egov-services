package org.egov.pgr.services;

import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.contract.BoundaryResponse;

public interface BoundaryService {

	BoundaryResponse fetchBoundaryByLatLng(RequestInfo requestInfo, Double lat, Double lng);

}
