package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.repository.ComplaintJpaRepository;
import org.egov.pgrrest.read.domain.model.Complaint;
import org.egov.pgrrest.read.domain.model.ComplaintSearchCriteria;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.read.persistence.specification.SevaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintRepository {
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private ComplaintJpaRepository complaintJpaRepository;
	private ComplaintMessageQueueRepository complaintMessageQueueRepository;

	@Autowired
	public ComplaintRepository(ComplaintJpaRepository complaintJpaRepository,
			ComplaintMessageQueueRepository complaintMessageQueueRepository) {
		this.complaintJpaRepository = complaintJpaRepository;
		this.complaintMessageQueueRepository = complaintMessageQueueRepository;
	}

    public void save(SevaRequest sevaRequest) {
        Date date = Calendar.getInstance().getTime();
        sevaRequest.getRequestInfo().setAction(POST);
        sevaRequest.getServiceRequest().setCreatedDate(date);
        this.complaintMessageQueueRepository.save(sevaRequest);
    }

	public List<Complaint> findAll(ComplaintSearchCriteria complaintSearchCriteria) {
		final SevaSpecification specification = new SevaSpecification(complaintSearchCriteria);
		final Sort sort = new Sort(Direction.DESC, "lastModifiedDate");
		return this.complaintJpaRepository.findAll(specification, sort).stream()
				.map(org.egov.pgrrest.common.entity.Complaint::toDomain).collect(Collectors.toList());
	}

    public void update(SevaRequest sevaRequest) {
        Date date = Calendar.getInstance().getTime();
        sevaRequest.getRequestInfo().setAction(PUT);
        sevaRequest.getServiceRequest().setLastModifiedDate(date);
        this.complaintMessageQueueRepository.save(sevaRequest);
    }

	public List<Complaint> getAllModifiedComplaintsForCitizen(Long userId, String tenantId) { 
		return this.complaintJpaRepository.getAllModifiedComplaintsForCitizen(userId, tenantId).stream()
				.map(org.egov.pgrrest.common.entity.Complaint::toDomain).collect(Collectors.toList());
	}
}
