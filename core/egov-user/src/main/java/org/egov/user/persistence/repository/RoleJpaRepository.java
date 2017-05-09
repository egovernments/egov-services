package org.egov.user.persistence.repository;

import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.RoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, RoleKey> {
    Role findByRoleKeyTenantIdAndCodeIgnoreCase(String tenantId, String name);
}
