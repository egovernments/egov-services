package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.BoundaryResponse;

public interface BoundaryService {

	BoundaryResponse fetchBoundaryById(Long id);

}
