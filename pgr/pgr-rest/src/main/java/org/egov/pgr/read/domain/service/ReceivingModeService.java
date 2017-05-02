package org.egov.pgr.read.domain.service;

import java.util.List;

import org.egov.pgr.read.domain.model.ReceivingMode;
import org.egov.pgr.read.persistence.repository.ReceivingModeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingModeService {

    private ReceivingModeRepository receivingModeRepository;
    @Autowired
    ReceivingModeService(ReceivingModeRepository receivingModeRepository)
    {
       this.receivingModeRepository=receivingModeRepository;
    }


    public List<ReceivingMode> getAllReceivingModes(String tenantId) {
        return receivingModeRepository.findAllByReceivingModeByTenantId (tenantId);
    }
}
