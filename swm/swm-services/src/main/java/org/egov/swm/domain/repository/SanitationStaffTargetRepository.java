package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.persistence.queue.repository.SanitationStaffTargetQueueRepository;
import org.egov.swm.persistence.repository.SanitationStaffTargetJdbcRepository;
import org.egov.swm.web.requests.SanitationStaffTargetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffTargetRepository {

	@Autowired
	private SanitationStaffTargetQueueRepository sanitationStaffTargetQueueRepository;

	@Autowired
	private SanitationStaffTargetJdbcRepository sanitationStaffTargetJdbcRepository;

	public SanitationStaffTargetRequest save(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		return sanitationStaffTargetQueueRepository.save(sanitationStaffTargetRequest);

	}

	public SanitationStaffTargetRequest update(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		return sanitationStaffTargetQueueRepository.update(sanitationStaffTargetRequest);

	}

	public Pagination<SanitationStaffTarget> search(SanitationStaffTargetSearch sanitationStaffTargetSearch) {
		return sanitationStaffTargetJdbcRepository.search(sanitationStaffTargetSearch);

	}

}