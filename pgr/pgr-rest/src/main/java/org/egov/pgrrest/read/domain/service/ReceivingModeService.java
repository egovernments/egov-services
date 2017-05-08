package org.egov.pgrrest.read.domain.service;

import java.util.List;


import org.egov.pgrrest.common.model.ReceivingMode;
import org.egov.pgrrest.read.persistence.repository.ReceivingModeRepository;
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
