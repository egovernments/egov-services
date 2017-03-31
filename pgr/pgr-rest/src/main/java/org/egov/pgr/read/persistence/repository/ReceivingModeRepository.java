
package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.read.persistence.entity.ReceivingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingModeRepository extends JpaRepository<ReceivingMode, Long> {
}
