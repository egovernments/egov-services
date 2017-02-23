package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.Assignment;
import org.springframework.stereotype.Service;

@Service
public class AssignmentRepository {

	public AssignmentRepository() {
	}

	public Assignment fetchAssignmentById(Long id) {
		// TODO : need to call assignment rest service
		return new Assignment();
	}

}
