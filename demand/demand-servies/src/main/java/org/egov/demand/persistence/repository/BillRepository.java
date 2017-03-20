package org.egov.demand.persistence.repository;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.EgBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository
		extends JpaRepository<EgBill, java.lang.Long>, JpaSpecificationExecutor<EgBill> {
	@Transactional
	EgBill save(EgBill bill);
}
