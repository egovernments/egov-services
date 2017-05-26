package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountJpaRepository
		extends JpaRepository<BankAccount, java.lang.Long>, JpaSpecificationExecutor<BankAccount> {

}