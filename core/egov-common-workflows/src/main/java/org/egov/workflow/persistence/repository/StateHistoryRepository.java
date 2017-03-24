package org.egov.workflow.persistence.repository;


import org.egov.workflow.persistence.entity.StateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface StateHistoryRepository extends JpaRepository<StateHistory,java.lang.Long>,JpaSpecificationExecutor<StateHistory>  {

}