
package org.egov.pgr.common.repository;

import java.util.List;

import org.egov.pgr.common.entity.ReceivingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingModeRepository extends JpaRepository<ReceivingMode, Long> {
    ReceivingMode findByCode(String code);
    
    List<ReceivingMode> findAllByTenantId(String tenantId);
}
