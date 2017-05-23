package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountEntityJpaRepository
		extends JpaRepository<AccountEntity, java.lang.Long>, JpaSpecificationExecutor<AccountEntity> {

	AccountEntity findByName(String name);

	AccountEntity findByCode(String code);

}