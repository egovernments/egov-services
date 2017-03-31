package org.egov.pgr.write.service;

import org.egov.pgr.common.entity.ComplaintStatus;
import org.egov.pgr.common.repository.ComplaintStatusJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintStatusWriteService {

    private final ComplaintStatusJpaRepository complaintStatusRepository;

    @Autowired
    public ComplaintStatusWriteService(final ComplaintStatusJpaRepository complaintStatusRepository) {
        this.complaintStatusRepository = complaintStatusRepository;
    }

    public ComplaintStatus getByName(final String name) {
        return complaintStatusRepository.findByName(name);
    }

}
