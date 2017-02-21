package org.egov.pgr.services;

import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.contract.CrossHierarchyResponse;

public interface CrossHierarchyService {

	CrossHierarchyResponse fetchCrossHierarchyById(RequestInfo requestInfo, String crossHierarchyId);

}
