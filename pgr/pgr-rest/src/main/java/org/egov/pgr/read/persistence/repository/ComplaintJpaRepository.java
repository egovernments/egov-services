package org.egov.pgr.read.persistence.repository;

import java.util.Date;

import javax.transaction.Transactional;
import java.util.List;

import org.egov.pgr.read.persistence.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintJpaRepository extends JpaRepository<Complaint, Long>, JpaSpecificationExecutor<Complaint> {

	@Modifying 
	@Query("update Complaint c set c.lastAccessedTime = :date where c.crn = :crn")
	@Transactional
    void updateLastAccessedTime(@Param("date") Date date,@Param("crn") String crn);

	@Query("from Complaint where createdBy=:userId and (lastAccessedTime is null or lastModifiedDate>=lastAccessedTime) order by lastModifiedDate desc")
	List<Complaint> getAllModifiedComplaintsForCitizen(@Param("userId") Long userId);

}
