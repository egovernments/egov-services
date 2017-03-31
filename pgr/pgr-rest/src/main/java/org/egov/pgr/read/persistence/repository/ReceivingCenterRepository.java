
package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.read.persistence.entity.ReceivingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingCenterRepository extends JpaRepository<ReceivingCenter, Long> {
	ReceivingCenter findById(Long id);
}
