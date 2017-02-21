package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.AccountDetailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface AccountDetailTypeRepository extends JpaRepository<AccountDetailType,java.lang.Long>,JpaSpecificationExecutor<AccountDetailType>  {

AccountDetailType findByName(String name);

}