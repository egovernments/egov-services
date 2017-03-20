package org.egov.demand.persistence.repository;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.EgBillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BillTypeRepository
		extends JpaRepository<EgBillType, java.lang.Long>, JpaSpecificationExecutor<EgBillType> {
	EgBillType findByName(String name);

	EgBillType findByCode(String code);

	@Transactional
	EgBillType save(EgBillType billType);
}
