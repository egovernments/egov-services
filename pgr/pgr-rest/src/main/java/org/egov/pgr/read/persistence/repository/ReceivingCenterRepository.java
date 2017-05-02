package org.egov.pgr.read.persistence.repository;


import org.egov.pgr.common.repository.ReceivingCenterJpaRepository;
import org.egov.pgr.read.domain.model.ReceivingCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceivingCenterRepository {


ReceivingCenterJpaRepository receivingCenterJpaRepository;

    @Autowired
    public ReceivingCenterRepository(ReceivingCenterJpaRepository receivingCenterJpaRepository) {
        this.receivingCenterJpaRepository = receivingCenterJpaRepository;
    }

    public ReceivingCenter findReceivingCenterByIdAndTenantId(Long id, String tenantId)
    {
            org.egov.pgr.common.entity.ReceivingCenter receivingCenter= receivingCenterJpaRepository.findByIdAndTenantId(id,tenantId);
                return receivingCenter.toDomain();
    }


 public List<ReceivingCenter> findAllReceivingCentersByTenantId(String tenantId)
    {
       return receivingCenterJpaRepository.findAllByTenantId(tenantId).stream().map(org.egov.pgr.common.entity.ReceivingCenter::toDomain).collect(Collectors.toList());
    }
}
