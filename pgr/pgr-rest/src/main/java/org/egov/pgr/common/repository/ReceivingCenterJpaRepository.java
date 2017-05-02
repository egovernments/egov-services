
package org.egov.pgr.common.repository;

import java.util.List;

import org.egov.pgr.common.entity.ReceivingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingCenterJpaRepository extends JpaRepository<ReceivingCenter, Long> {
    ReceivingCenter findByIdAndTenantId(Long id, String tenantId);

    List<ReceivingCenter> findAllByTenantId(String tenantId);
}
