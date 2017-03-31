package org.egov.pgr.write.service;

import org.egov.pgr.common.entity.ComplaintType;
import org.egov.pgr.common.repository.ComplaintTypeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeWriteService {

    private final ComplaintTypeJpaRepository complaintTypeWriteRepository;

    @Autowired
    public ComplaintTypeWriteService(final ComplaintTypeJpaRepository complaintTypeWriteRepository) {
        this.complaintTypeWriteRepository = complaintTypeWriteRepository;
    }

    public ComplaintType findByCode(final String code) {
        return complaintTypeWriteRepository.findByCode(code);
    }
}
