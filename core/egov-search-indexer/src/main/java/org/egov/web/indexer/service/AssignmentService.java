package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.Assignment;

public interface AssignmentService {
	Assignment fetchAssignmentById(Long id);
}
