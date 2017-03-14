package org.egov.user.persistence.repository;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.specification.UserSearchSpecificationFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private UserJpaRepository userJpaRepository;
    private UserSearchSpecificationFactory userSearchSpecificationFactory;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public UserRepository(UserJpaRepository userJpaRepository,
                          UserSearchSpecificationFactory userSearchSpecificationFactory,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.userSearchSpecificationFactory = userSearchSpecificationFactory;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String userName) {
        return userJpaRepository.findByUsername(userName);
    }

    public User findByEmailId(String emailId) {
        return userJpaRepository.findByEmailId(emailId);
    }

    public User save(org.egov.user.domain.model.User domainUser) {
        User entityUser = new User(domainUser);
        setEnrichedRolesToUser(entityUser);
        encryptPassword(entityUser);
        return userJpaRepository.save(entityUser);
    }

    private void encryptPassword(User entityUser) {
        final String encodedPassword = passwordEncoder.encode(entityUser.getPassword());
        entityUser.setPassword(encodedPassword);
    }

    public List<User> findAll(UserSearch userSearch) {
        Specification<User> specification = userSearchSpecificationFactory.getSpecification(userSearch);
        return userJpaRepository.findAll(specification);
    }

    private Set<Role> fetchRolesByName(User user) {
        return user.getRoles()
                .stream()
                .map((role) -> roleRepository.findByNameContainingIgnoreCase(role.getName()))
                .collect(Collectors.toSet());
    }

    private void setEnrichedRolesToUser(User user) {
        final Set<Role> roles = fetchRolesByName(user);
        user.setRoles(roles);
    }
}
