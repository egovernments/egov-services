package org.egov.boundary.persistence.repository;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

import javax.persistence.QueryHint;

import org.egov.boundary.persistence.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	@QueryHints({ @QueryHint(name = HINT_CACHEABLE, value = "true") })
	City findByCodeAndTenantId(String code, String tenantId);

	City findByIdAndTenantId(Long id, String tenantId);
}