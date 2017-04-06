package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.persistence.repository.ComplaintStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintStatusService {

    ComplaintStatusRepository complaintStatusRepository;

    public ComplaintStatusService(ComplaintStatusRepository complaintStatusRepository) {
        this.complaintStatusRepository = complaintStatusRepository;
    }

    public List<ComplaintStatus> findAll() {
        return complaintStatusRepository.findAll();
    }
}
