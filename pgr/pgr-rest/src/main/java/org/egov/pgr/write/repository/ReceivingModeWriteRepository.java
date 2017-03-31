package org.egov.pgr.write.repository;

import org.egov.pgr.write.entity.ReceivingMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingModeWriteRepository extends JpaRepository<ReceivingMode, Long> {

	ReceivingMode findByCode(String code);
}
