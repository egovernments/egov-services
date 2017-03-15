package org.egov.pgr.persistence.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.persistence.specification.SevaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class ComplaintRepository {
	private static final String CREATE = "create";
	private static final String UPDATE = "update";
	private ComplaintJpaRepository complaintJpaRepository;
	private ComplaintMessageQueueRepository complaintMessageQueueRepository;

	@Autowired
	public ComplaintRepository(ComplaintJpaRepository complaintJpaRepository,
			ComplaintMessageQueueRepository complaintMessageQueueRepository) {
		this.complaintJpaRepository = complaintJpaRepository;
		this.complaintMessageQueueRepository = complaintMessageQueueRepository;
	}

	public void save(SevaRequest sevaRequest) {
		sevaRequest.getRequestInfo().setAction(CREATE);
		this.complaintMessageQueueRepository.save(sevaRequest);
	}

	public List<Complaint> findAll(ComplaintSearchCriteria complaintSearchCriteria) {
		final SevaSpecification specification = new SevaSpecification(complaintSearchCriteria);
		return this.complaintJpaRepository.findAll(specification, new Sort(Direction.DESC, "lastModifiedDate")).stream()
				.map(org.egov.pgr.persistence.entity.Complaint::toDomain).collect(Collectors.toList());
	}

	public void update(SevaRequest sevaRequest) {
		sevaRequest.getRequestInfo().setAction(UPDATE);
		this.complaintMessageQueueRepository.save(sevaRequest);
	}

	public List<Complaint> getAllComplaintsForGivenUser(Long userId) {
		return this.complaintJpaRepository.findByCreatedByOrderByLastAccessedTimeDesc(userId).stream()
				.map(org.egov.pgr.persistence.entity.Complaint::toDomain).collect(Collectors.toList());

	}

	public List<Complaint> getAllModifiedComplaintsForCitizen(Date lastAccessedTime, Long userId) {
		return this.complaintJpaRepository.getAllModifiedComplaintsForCitizen(lastAccessedTime, userId).stream()
				.map(org.egov.pgr.persistence.entity.Complaint::toDomain).collect(Collectors.toList());
	}
}
