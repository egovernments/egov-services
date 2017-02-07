package org.egov.egf.repository;


import org.egov.egf.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface BankRepository extends JpaRepository<BankEntity,java.lang.Long> {

	BankEntity findByName(String name);

	BankEntity findByCode(String code);

}