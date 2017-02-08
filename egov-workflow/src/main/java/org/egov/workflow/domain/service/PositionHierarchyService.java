package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.domain.model.PositionHierarchyResponse;

public interface PositionHierarchyService {

	List<PositionHierarchyResponse> getByObjectTypeObjectSubTypeAndFromPosition(String objectType, String objectSubType,
			Long fromPositionid);

}
