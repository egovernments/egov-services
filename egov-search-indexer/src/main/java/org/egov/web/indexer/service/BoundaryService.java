package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.Boundary;

public interface BoundaryService {
	Boundary fetchBoundaryById(Long id);
}
