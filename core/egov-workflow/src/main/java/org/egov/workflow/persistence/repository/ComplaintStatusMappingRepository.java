package org.egov.workflow.persistence.repository;

import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.persistence.entity.ComplaintStatusMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintStatusMappingRepository {

    private ComplaintStatusMappingJpaRepository complaintStatusMappingJpaRepository;

    public ComplaintStatusMappingRepository(ComplaintStatusMappingJpaRepository complaintStatusMappingJpaRepository) {
        this.complaintStatusMappingJpaRepository = complaintStatusMappingJpaRepository;
    }

    public List<ComplaintStatus> getNextStatuses(Long currentStatusId, List<Long> roles) {
        return complaintStatusMappingJpaRepository
                .findByCurrentStatusIdAndRoleInOrderByOrderNoAsc(currentStatusId, roles).stream()
                .map(ComplaintStatusMapping::getShowStatus)
                .map(org.egov.workflow.persistence.entity.ComplaintStatus::toDomain)
                .distinct()
                .collect(Collectors.toList());
    }
}
