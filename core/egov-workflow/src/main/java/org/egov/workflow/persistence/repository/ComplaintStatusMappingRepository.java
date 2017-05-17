package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatusMapping;
import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintStatusMappingRepository {

    private ComplaintStatusMappingJpaRepository complaintStatusMappingJpaRepository;

    public ComplaintStatusMappingRepository(ComplaintStatusMappingJpaRepository complaintStatusMappingJpaRepository) {
        this.complaintStatusMappingJpaRepository = complaintStatusMappingJpaRepository;
    }

    public List<org.egov.workflow.domain.model.ComplaintStatus> getNextStatuses(Long currentStatusId
        , List<Long> roles, String tenantId) {
        return complaintStatusMappingJpaRepository
                .findByCurrentStatusIdAndRoleInAndTenantIdOrderByOrderNoAsc(currentStatusId, roles,tenantId).stream()
                .map(ComplaintStatusMapping::getShowStatus)
                .map(ComplaintStatus::toDomain)
                .distinct()
                .collect(Collectors.toList());
    }
}
