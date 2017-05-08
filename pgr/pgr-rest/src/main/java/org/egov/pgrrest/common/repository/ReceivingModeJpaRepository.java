
package org.egov.pgrrest.common.repository;

import java.util.List;

import org.egov.pgrrest.common.entity.ReceivingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingModeJpaRepository extends JpaRepository<ReceivingMode, Long> {

    ReceivingMode findByCode(String code);
    
    List<ReceivingMode> findAllByTenantId(String tenantId);
}
