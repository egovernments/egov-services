
package org.egov.pgr.common.repository;

import org.egov.pgr.common.entity.ReceivingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingCenterRepository extends JpaRepository<ReceivingCenter, Long> {
	ReceivingCenter findById(Long id);
}
