package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintStatusRepository {

    private ComplaintStatusJpaRepository complaintStatusJpaRepository;

    public ComplaintStatusRepository(ComplaintStatusJpaRepository complaintStatusJpaRepository) {
        this.complaintStatusJpaRepository = complaintStatusJpaRepository;
    }

    public List<org.egov.workflow.domain.model.ComplaintStatus> findAll() {
        return complaintStatusJpaRepository.findAll()
                .stream()
                .map(ComplaintStatus::toDomain)
                .collect(Collectors.toList());
    }

    public org.egov.workflow.domain.model.ComplaintStatus findByName(String name) {
        return complaintStatusJpaRepository.findByName(name).toDomain();
    }

    public org.egov.workflow.domain.model.ComplaintStatus findByCodeAndTenantId(String code, String tenantId){
        return complaintStatusJpaRepository.findByCodeAndTenantId(code,tenantId).toDomain();
    }
}

