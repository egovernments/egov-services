package org.egov.user.persistence.repository;

import org.egov.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @QueryHints({@QueryHint(name = HINT_CACHEABLE, value = "true")})
    User findByUsername(String userName);

    User findByEmailId(String emailId);

    @Query("select count(u.id) from User u where u.username = :username and u.id <> :id and u.tenantId = :tenantId")
    Long isUserPresent(@Param("username") String userName, @Param("id") Long notMatchingId,
					   @Param("tenantId") String tenantId);

	@Query("select count(u.id) from User u where u.username = :username and u.tenantId = :tenantId")
	Long isUserPresent(@Param("username") String userName, @Param("tenantId") String tenantId);

}

