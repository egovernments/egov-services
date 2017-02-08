package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.entity.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintTypeJpaRepository extends JpaRepository<ComplaintType, Long> {
    List<ComplaintType> findByIsActiveTrueAndCategoryIdOrderByNameAsc(Long categoryId);

    ComplaintType findByCode(String code);
}
