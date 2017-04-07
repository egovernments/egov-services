package org.egov.workflow.persistence.repository;

import org.egov.workflow.domain.model.ComplaintStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintStatusRepository {

    private ComplaintStatusJpaRepository complaintStatusJpaRepository;

    public ComplaintStatusRepository(ComplaintStatusJpaRepository complaintStatusJpaRepository) {
        this.complaintStatusJpaRepository = complaintStatusJpaRepository;
    }

    public List<ComplaintStatus> findAll() {
        return complaintStatusJpaRepository.findAll()
                .stream()
                .map(org.egov.workflow.persistence.entity.ComplaintStatus::toDomain)
                .collect(Collectors.toList());
    }

    public ComplaintStatus findByName(String name) {
        return complaintStatusJpaRepository.findByName(name).toDomain();
    }
}

