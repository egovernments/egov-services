package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Fundsource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FundsourceJpaRepository
		extends JpaRepository<Fundsource, java.lang.Long>, JpaSpecificationExecutor<Fundsource> {

	Fundsource findByName(String name);

	Fundsource findByCode(String code);

}