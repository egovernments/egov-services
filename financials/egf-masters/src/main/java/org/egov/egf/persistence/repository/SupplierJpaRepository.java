package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierJpaRepository
		extends JpaRepository<Supplier, java.lang.Long>, JpaSpecificationExecutor<Supplier> {

	Supplier findByName(String name);

	Supplier findByCode(String code);

}