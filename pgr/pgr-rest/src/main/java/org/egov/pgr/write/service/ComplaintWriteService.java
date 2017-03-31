package org.egov.pgr.write.service;

import org.egov.pgr.write.entity.Complaint;
import org.egov.pgr.write.repository.ComplaintWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

@Service
@Transactional
public class ComplaintWriteService {

    private ComplaintWriteRepository complaintWriteRepository;

    @Autowired
    public ComplaintWriteService(ComplaintWriteRepository complaintWriteRepository) {
        this.complaintWriteRepository = complaintWriteRepository;
    }

    public Complaint findByCrn(String complaintCrn) {
        return complaintWriteRepository.findByCrn(complaintCrn);
    }

    @Transactional
    public Complaint save(Complaint complaint) throws ValidationException {
        complaintWriteRepository.save(complaint);
        return complaint;
    }
}