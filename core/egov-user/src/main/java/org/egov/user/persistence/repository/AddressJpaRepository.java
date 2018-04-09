package org.egov.user.persistence.repository;

import org.egov.user.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Long> {
	List<Address> findByUserIdAndTenantId(Long userId, String tenantId);
}

