package org.egov.pgr.write.service;

import org.egov.pgr.write.model.ComplaintRecord;
import org.egov.pgr.write.repository.ComplaintWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplaintWriteService {

    private ComplaintWriteRepository complaintWriteRepository;

    @Autowired
    public ComplaintWriteService(ComplaintWriteRepository complaintWriteRepository) {
        this.complaintWriteRepository = complaintWriteRepository;
    }

    public void updateOrInsert(ComplaintRecord complaintRecord) {
        complaintWriteRepository.updateOrInsert(complaintRecord);
    }
}