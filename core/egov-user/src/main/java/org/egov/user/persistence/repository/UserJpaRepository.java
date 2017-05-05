package org.egov.user.persistence.repository;

import org.egov.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsernameAndTenantId(String userName, String tenantId);

    User findByEmailIdAndTenantId(String emailId, String tenantId);

    @Query("select count(u.id) from User u where u.username = :username and u.id <> :id and u.tenantId = :tenantId")
    Long isUserPresent(@Param("username") String userName, @Param("id") Long notMatchingId,
					   @Param("tenantId") String tenantId);

	@Query("select count(u.id) from User u where u.username = :username and u.tenantId = :tenantId")
	Long isUserPresent(@Param("username") String userName, @Param("tenantId") String tenantId);

}

