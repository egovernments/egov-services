package org.egov.workflow.domain.service;

import org.egov.workflow.domain.exception.InvalidComplaintStatusException;
import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;
import org.egov.workflow.persistence.repository.ComplaintStatusMappingRepository;
import org.egov.workflow.persistence.repository.ComplaintStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintStatusService {

    private ComplaintStatusRepository complaintStatusRepository;
    private ComplaintStatusMappingRepository complaintStatusMappingRepository;

    public ComplaintStatusService(ComplaintStatusRepository complaintStatusRepository,
                                  ComplaintStatusMappingRepository complaintStatusMappingRepository) {
        this.complaintStatusRepository = complaintStatusRepository;
        this.complaintStatusMappingRepository = complaintStatusMappingRepository;
    }

    public List<ComplaintStatus> findAll() {
        return complaintStatusRepository.findAll();
    }

    public List<ComplaintStatus> getNextStatuses(ComplaintStatusSearchCriteria complaintStatusSearchCriteria) {
        complaintStatusSearchCriteria.validate();

        ComplaintStatus currentStatus =
                complaintStatusRepository.findByCodeAndTenantId(
                    complaintStatusSearchCriteria.getComplaintStatusCode(),
                    complaintStatusSearchCriteria.getTenantId());

        if (currentStatus == null) {
            throw new InvalidComplaintStatusException();
        }

        return complaintStatusMappingRepository
                .getNextStatuses(currentStatus.getId(), complaintStatusSearchCriteria.getRoles(),
                complaintStatusSearchCriteria.getTenantId());
    }
}
