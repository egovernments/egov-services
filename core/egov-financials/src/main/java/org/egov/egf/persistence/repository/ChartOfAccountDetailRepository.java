package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface ChartOfAccountDetailRepository extends JpaRepository<ChartOfAccountDetail,java.lang.Long>,JpaSpecificationExecutor<ChartOfAccountDetail>  {

}