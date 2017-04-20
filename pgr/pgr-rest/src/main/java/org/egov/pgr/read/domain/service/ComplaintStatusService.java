package org.egov.pgr.read.domain.service;

import org.egov.pgr.common.entity.ComplaintStatus;
import org.egov.pgr.common.repository.ComplaintStatusJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintStatusService {

    private final ComplaintStatusJpaRepository complaintStatusRepository;

    @Autowired
    public ComplaintStatusService(final ComplaintStatusJpaRepository complaintStatusRepository) {
        this.complaintStatusRepository = complaintStatusRepository;
    }

    public List<ComplaintStatus> getAllComplaintStatusByTenantId(final String tenantId) {
        return complaintStatusRepository.findAllByTenantId(tenantId);
    }

}
