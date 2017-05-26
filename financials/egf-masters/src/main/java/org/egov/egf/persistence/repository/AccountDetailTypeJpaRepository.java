package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailTypeJpaRepository
		extends JpaRepository<AccountDetailType, java.lang.Long>, JpaSpecificationExecutor<AccountDetailType> {

	AccountDetailType findByName(String name);

}