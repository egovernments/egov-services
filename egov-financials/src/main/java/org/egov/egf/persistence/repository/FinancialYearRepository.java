package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.FinancialYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface FinancialYearRepository extends JpaRepository<FinancialYear,java.lang.Long>,JpaSpecificationExecutor<FinancialYear>  {

}