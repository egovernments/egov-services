package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface AccountCodePurposeRepository extends JpaRepository<AccountCodePurpose,java.lang.Long>,JpaSpecificationExecutor<AccountCodePurpose>  {

AccountCodePurpose findByName(String name);

}