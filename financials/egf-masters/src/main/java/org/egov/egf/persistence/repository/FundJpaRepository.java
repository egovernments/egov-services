package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FundJpaRepository extends JpaRepository<Fund, java.lang.Long>, JpaSpecificationExecutor<Fund> {

	Fund findByName(String name);

	Fund findByCode(String code);

}