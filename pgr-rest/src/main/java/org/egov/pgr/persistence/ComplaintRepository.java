package org.egov.pgr.persistence;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.persistence.specification.SevaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintRepository {
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private ComplaintJpaRepository complaintJpaRepository;
    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    @Autowired
    public ComplaintRepository(ComplaintJpaRepository complaintJpaRepository,
                               ComplaintMessageQueueRepository
                                       complaintMessageQueueRepository) {
        this.complaintJpaRepository = complaintJpaRepository;
        this.complaintMessageQueueRepository = complaintMessageQueueRepository;
    }

    public void save(Complaint complaint) {
        final org.egov.pgr.persistence.queue.contract.Complaint complaintDto =
                new org.egov.pgr.persistence.queue.contract.Complaint(complaint, CREATE);
        this.complaintMessageQueueRepository.save(complaintDto);
    }

    public List<Complaint> findAll(ComplaintSearchCriteria complaintSearchCriteria) {
        final SevaSpecification specification = new SevaSpecification(complaintSearchCriteria);
        return this.complaintJpaRepository.findAll(specification)
                .stream()
                .map(org.egov.pgr.persistence.entity.Complaint::toDomain)
                .collect(Collectors.toList());
    }

    public void update(Complaint complaint) {
        final org.egov.pgr.persistence.queue.contract.Complaint complaintDto =
                new org.egov.pgr.persistence.queue.contract.Complaint(complaint, UPDATE);
        this.complaintMessageQueueRepository.save(complaintDto);
    }
}

