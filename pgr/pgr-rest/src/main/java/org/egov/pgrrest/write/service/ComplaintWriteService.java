package org.egov.pgrrest.write.service;

import org.egov.pgrrest.write.model.ComplaintRecord;
import org.egov.pgrrest.write.repository.ComplaintWriteRepository;
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