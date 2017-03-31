package org.egov.pgr.write.service;

import org.egov.pgr.write.entity.ComplaintType;
import org.egov.pgr.write.repository.ComplaintTypeWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeWriteService {

    private final ComplaintTypeWriteRepository complaintTypeWriteRepository;

    @Autowired
    public ComplaintTypeWriteService(final ComplaintTypeWriteRepository complaintTypeWriteRepository) {
        this.complaintTypeWriteRepository = complaintTypeWriteRepository;
    }

    public ComplaintType findByCode(final String code) {
        return complaintTypeWriteRepository.findByCode(code);
    }
}
