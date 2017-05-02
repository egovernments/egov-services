package org.egov.pgr.read.persistence.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.common.entity.ReceivingMode;
import org.egov.pgr.common.repository.ReceivingModeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingModeRepository {

    private ReceivingModeJpaRepository receivingModeJpaRepository;

    @Autowired
    public ReceivingModeRepository(ReceivingModeJpaRepository receivingModeJpaRepository) {
        this.receivingModeJpaRepository = receivingModeJpaRepository;
    }
    
    public List<org.egov.pgr.read.domain.model.ReceivingMode> findAllByReceivingModeByTenantId (String tenantId) {
    	return receivingModeJpaRepository.findAllByTenantId(tenantId).stream().map(ReceivingMode::toDomain)
            .collect(Collectors.toList());
    }
}


