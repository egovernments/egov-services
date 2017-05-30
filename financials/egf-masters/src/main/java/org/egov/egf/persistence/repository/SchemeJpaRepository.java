package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeJpaRepository extends JpaRepository<Scheme, java.lang.Long>, JpaSpecificationExecutor<Scheme> {

	Scheme findByName(String name);

	Scheme findByCode(String code);

}