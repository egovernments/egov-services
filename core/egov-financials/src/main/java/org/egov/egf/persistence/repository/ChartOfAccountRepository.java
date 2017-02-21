package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount,java.lang.Long>,JpaSpecificationExecutor<ChartOfAccount>  {

ChartOfAccount findByName(String name);

}