package org.egov.user.persistence.repository;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.specification.UserSearchSpecificationFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private UserJpaRepository userJpaRepository;
    private UserSearchSpecificationFactory userSearchSpecificationFactory;

    public UserRepository(UserJpaRepository userJpaRepository,
                          UserSearchSpecificationFactory userSearchSpecificationFactory) {
        this.userJpaRepository = userJpaRepository;
        this.userSearchSpecificationFactory = userSearchSpecificationFactory;
    }

    public User findByUsername(String userName) {
        return userJpaRepository.findByUsername(userName);
    }

    public User findByEmailId(String emailId) {
        return userJpaRepository.findByEmailId(emailId);
    }

    public User save(User user) {
        return userJpaRepository.save(user);
    }

    public List<User> findAll(UserSearch userSearch) {
        Specification<User> specification = userSearchSpecificationFactory.getSpecification(userSearch);
        return userJpaRepository.findAll(specification);
    }
}
