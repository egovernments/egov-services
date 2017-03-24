package org.egov.workflow.persistence.repository;


import org.egov.workflow.persistence.entity.WorkFlowMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface WorkFlowMatrixRepository extends JpaRepository<WorkFlowMatrix,java.lang.Long>,JpaSpecificationExecutor<WorkFlowMatrix>  {

}