package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface AccountEntityRepository extends JpaRepository<AccountEntity,java.lang.Long>,JpaSpecificationExecutor<AccountEntity>  {

AccountEntity findByName(String name);

AccountEntity findByCode(String code);

}