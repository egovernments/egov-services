package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Functionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionaryJpaRepository
		extends JpaRepository<Functionary, java.lang.Long>, JpaSpecificationExecutor<Functionary> {

	Functionary findByName(String name);

	Functionary findByCode(String code);

}