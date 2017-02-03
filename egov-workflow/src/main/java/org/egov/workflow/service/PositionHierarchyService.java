package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.model.PositionHierarchyResponse;

public interface PositionHierarchyService {

	List<PositionHierarchyResponse> getByObjectTypeObjectSubTypeAndFromPosition(String objectType, String objectSubType,
			Long fromPositionid);

}
