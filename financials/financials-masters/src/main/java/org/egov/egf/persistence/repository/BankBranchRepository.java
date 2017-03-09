package org.egov.egf.persistence.repository;


import org.egov.egf.persistence.entity.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository 
public interface BankBranchRepository extends JpaRepository<BankBranch,java.lang.Long>,JpaSpecificationExecutor<BankBranch>  {

BankBranch findByName(String name);

BankBranch findByCode(String code);

}