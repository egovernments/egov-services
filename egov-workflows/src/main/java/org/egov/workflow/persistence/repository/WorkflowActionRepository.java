package org.egov.workflow.persistence.repository;


import org.egov.workflow.persistence.entity.WorkflowAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface WorkflowActionRepository extends JpaRepository<WorkflowAction,java.lang.Long>,JpaSpecificationExecutor<WorkflowAction>  {

WorkflowAction findByName(String name);

}