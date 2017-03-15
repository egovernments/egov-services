package org.egov.user.persistence.repository;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.specification.UserSearchSpecificationFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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

    public boolean isUserPresent(String userName) {
        return userJpaRepository.isUserPresent(userName) != null;
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

    public List<User> findAll(UserSearch userSearch) {
        Specification<User> specification = userSearchSpecificationFactory.getSpecification(userSearch);
        PageRequest pageRequest = createPageRequest(userSearch);
        return userJpaRepository.findAll(specification, pageRequest).getContent();
    }

    private void encryptPassword(User entityUser) {
        final String encodedPassword = passwordEncoder.encode(entityUser.getPassword());
        entityUser.setPassword(encodedPassword);
    }

    private PageRequest createPageRequest(UserSearch userSearch) {
        Sort sort = createSort(userSearch);
        return new PageRequest(userSearch.getPageNumber(), userSearch.getPageSize(), sort);
    }

    private Sort createSort(UserSearch userSearch) {
        List<String> sortFields = Arrays.asList("username", "name", "gender");
        List<Sort.Order> orders = userSearch.getSort()
                .stream()
                .limit(3)
                .map(String::toLowerCase)
                .filter(sortFields::contains)
                .map(property -> new Sort.Order(Sort.Direction.ASC, property))
                .collect(Collectors.toList());
        return new Sort(orders);
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
