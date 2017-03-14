package org.egov.user.persistence.repository;

import org.egov.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @QueryHints({ @QueryHint(name = HINT_CACHEABLE, value = "true") })
    User findByUsername(String userName);

    User findByEmailId(String emailId);

    @Transactional
    User save(User user);
}
