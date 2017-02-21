package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.SubScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository 
public interface SubSchemeRepository extends JpaRepository<SubScheme,java.lang.Long>,JpaSpecificationExecutor<SubScheme>  {

SubScheme findByName(String name);

SubScheme findByCode(String code);

}