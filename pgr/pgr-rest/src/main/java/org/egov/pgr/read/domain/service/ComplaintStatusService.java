package org.egov.pgr.read.domain.service;

import java.util.List;

import org.egov.pgr.read.persistence.entity.ComplaintStatus;
import org.egov.pgr.read.persistence.repository.ComplaintStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintStatusService {

    private final ComplaintStatusRepository complaintStatusRepository;

    @Autowired
    public ComplaintStatusService(final ComplaintStatusRepository complaintStatusRepository) {
        this.complaintStatusRepository = complaintStatusRepository;
    }

    public ComplaintStatus load(final Long id) {
        return complaintStatusRepository.getOne(id);
    }

    public ComplaintStatus getByName(final String name) {
        return complaintStatusRepository.findByName(name);
    }

    public List<ComplaintStatus> getAllComplaintStatus() {
        return complaintStatusRepository.findAll();
    }

}
