package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.AccountDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface AccountDetailKeyRepository extends JpaRepository<AccountDetailKey,java.lang.Long>,JpaSpecificationExecutor<AccountDetailKey>  {

AccountDetailKey findByName(String name);

}