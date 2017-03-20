package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankJpaRepository extends JpaRepository<Bank, java.lang.Long>, JpaSpecificationExecutor<Bank> {

	Bank findByName(String name);

	Bank findByCode(String code);

}