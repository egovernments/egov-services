package org.egov.pgrrest.read.persistence.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgrrest.common.entity.ReceivingMode;
import org.egov.pgrrest.common.repository.ReceivingModeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingModeRepository {

    private ReceivingModeJpaRepository receivingModeJpaRepository;

    @Autowired
    public ReceivingModeRepository(ReceivingModeJpaRepository receivingModeJpaRepository) {
        this.receivingModeJpaRepository = receivingModeJpaRepository;
    }
    
    public List<org.egov.pgrrest.common.model.ReceivingMode> findAllByReceivingModeByTenantId (String tenantId) {
    	return receivingModeJpaRepository.findAllByTenantId(tenantId).stream().map(ReceivingMode::toDomain)
            .collect(Collectors.toList());
    }
}


