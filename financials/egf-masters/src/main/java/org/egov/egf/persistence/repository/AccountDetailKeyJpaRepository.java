package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.AccountDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailKeyJpaRepository
		extends JpaRepository<AccountDetailKey, java.lang.Long>, JpaSpecificationExecutor<AccountDetailKey> {

}