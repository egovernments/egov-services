package org.egov.pgr.write.repository;

import org.egov.pgr.write.entity.ReceivingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingCenterWriteRepository extends JpaRepository<ReceivingCenter, Long> {

	ReceivingCenter findById(Long id);
}
