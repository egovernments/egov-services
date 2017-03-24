package org.egov.workflow.persistence.repository;


import org.egov.workflow.persistence.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface StateRepository extends JpaRepository<State,java.lang.Long>,JpaSpecificationExecutor<State>  {

}