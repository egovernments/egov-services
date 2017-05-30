package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionJpaRepository
		extends JpaRepository<Function, java.lang.Long>, JpaSpecificationExecutor<Function> {

	Function findByName(String name);

	Function findByCode(String code);

}