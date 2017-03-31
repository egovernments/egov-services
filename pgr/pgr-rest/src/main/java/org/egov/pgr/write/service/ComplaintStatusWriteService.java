package org.egov.pgr.write.service;

import org.egov.pgr.write.entity.ComplaintStatus;
import org.egov.pgr.write.repository.ComplaintStatusWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintStatusWriteService {

    private final ComplaintStatusWriteRepository complaintStatusWriteRepository;

    @Autowired
    public ComplaintStatusWriteService(final ComplaintStatusWriteRepository complaintStatusWriteRepository) {
        this.complaintStatusWriteRepository = complaintStatusWriteRepository;
    }

    public ComplaintStatus getByName(final String name) {
        return complaintStatusWriteRepository.findByName(name);
    }

}
