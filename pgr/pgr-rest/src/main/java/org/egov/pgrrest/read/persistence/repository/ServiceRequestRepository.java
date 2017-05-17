package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.repository.ComplaintJpaRepository;
import org.egov.pgrrest.common.repository.SubmissionJpaRepository;
import org.egov.pgrrest.read.domain.model.Complaint;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.read.persistence.specification.ComplaintSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRequestRepository {
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private ComplaintJpaRepository complaintJpaRepository;
	private ServiceRequestMessageQueueRepository serviceRequestMessageQueueRepository;
	private SubmissionJpaRepository submissionJpaRepository;

	@Autowired
	public ServiceRequestRepository(ComplaintJpaRepository complaintJpaRepository,
                                    ServiceRequestMessageQueueRepository serviceRequestMessageQueueRepository) {
		this.complaintJpaRepository = complaintJpaRepository;
		this.serviceRequestMessageQueueRepository = serviceRequestMessageQueueRepository;
	}

    public void save(SevaRequest sevaRequest) {
        Date date = Calendar.getInstance().getTime();
        sevaRequest.getRequestInfo().setAction(POST);
        sevaRequest.getServiceRequest().setCreatedDate(date);
        this.serviceRequestMessageQueueRepository.save(sevaRequest);
    }

	public List<Complaint> findAll(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
		final ComplaintSpecification specification = new ComplaintSpecification(serviceRequestSearchCriteria);
		final Sort sort = new Sort(Direction.DESC, "lastModifiedDate");
		return this.complaintJpaRepository.findAll(specification, sort).stream()
				.map(org.egov.pgrrest.common.entity.Complaint::toDomain).collect(Collectors.toList());
	}

    public void update(SevaRequest sevaRequest) {
        Date date = Calendar.getInstance().getTime();
        sevaRequest.getRequestInfo().setAction(PUT);
        sevaRequest.getServiceRequest().setLastModifiedDate(date);
        this.serviceRequestMessageQueueRepository.save(sevaRequest);
    }

	public List<Complaint> getAllModifiedServiceRequestsForCitizen(Long userId, String tenantId) {
		return this.complaintJpaRepository.getAllModifiedComplaintsForCitizen(userId, tenantId).stream()
				.map(org.egov.pgrrest.common.entity.Complaint::toDomain).collect(Collectors.toList());
	}
}
