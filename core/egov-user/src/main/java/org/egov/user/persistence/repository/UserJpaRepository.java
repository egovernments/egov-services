package org.egov.user.persistence.repository;

import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.UserKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UserKey>, JpaSpecificationExecutor<User> {
    User findByUsernameAndUserKeyTenantId(String userName, String tenantId);

    User findByEmailIdAndUserKeyTenantId(String emailId, String tenantId);

	User findByUserKeyIdAndUserKeyTenantId(Long id, String tenantId);

	@Query("select count(u.userKey.id) from User u where u.username = :username and u.userKey.id <> :id and u.userKey.tenantId = :tenantId")
    Long isUserPresent(@Param("username") String userName, @Param("id") Long notMatchingId,
					   @Param("tenantId") String tenantId);

	@Query("select count(u.userKey.id) from User u where u.username = :username and u.userKey.tenantId = :tenantId")
	Long isUserPresent(@Param("username") String userName, @Param("tenantId") String tenantId);

}

