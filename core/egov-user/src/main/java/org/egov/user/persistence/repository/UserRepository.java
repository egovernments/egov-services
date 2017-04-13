package org.egov.user.persistence.repository;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.enums.BloodGroup;
import org.egov.user.persistence.enums.Gender;
import org.egov.user.persistence.enums.GuardianRelation;
import org.egov.user.persistence.enums.UserType;
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

import static org.egov.user.persistence.entity.EnumConverter.toEnumType;
import static org.springframework.util.ObjectUtils.isEmpty;

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

    public org.egov.user.domain.model.User findByUsername(String userName) {
        final User entityUser = userJpaRepository.findByUsername(userName);
        return entityUser != null ? entityUser.toDomain() : null;
    }

    public boolean isUserPresent(String userName, Long id, String tenantId) {
        return userJpaRepository.isUserPresent(userName, id, tenantId) != null;
    }

    public org.egov.user.domain.model.User findByEmailId(String emailId) {
        final User entityUser = userJpaRepository.findByEmailId(emailId);
        return entityUser != null ? entityUser.toDomain() : null;
    }

    public org.egov.user.domain.model.User save(org.egov.user.domain.model.User domainUser) {
        User entityUser = new User(domainUser);
        setEnrichedRolesToUser(entityUser);
        encryptPassword(entityUser);
        return userJpaRepository.save(entityUser).toDomain();
    }

    public List<org.egov.user.domain.model.User> findAll(UserSearch userSearch) {
        Specification<User> specification = userSearchSpecificationFactory.getSpecification(userSearch);
        PageRequest pageRequest = createPageRequest(userSearch);
        List<User> userEntities = userJpaRepository.findAll(specification, pageRequest).getContent();
        return userEntities.stream().map(User::toDomain).collect(Collectors.toList());
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
                .map((role) -> roleRepository.findByTenantIdAndNameIgnoreCase(user.getTenantId(),role.getName()))
                .collect(Collectors.toSet());
    }

    private void setEnrichedRolesToUser(User user) {
        final Set<Role> roles = fetchRolesByName(user);
        user.setRoles(roles);
    }
    
    public User getUserById(final Long id) {
        return userJpaRepository.findOne(id);
    }

    public org.egov.user.domain.model.User update(final Long id, final org.egov.user.domain.model.User user) {
        User oldUser = userJpaRepository.findOne(id);
        if (!isEmpty(user.getAadhaarNumber()))
            oldUser.setAadhaarNumber(user.getAadhaarNumber());
        if (!isEmpty(user.getAccountLocked()))
            oldUser.setAccountLocked(user.getAccountLocked());
        if (!isEmpty(user.getActive()))
            oldUser.setActive(user.getActive());
        if (!isEmpty(user.getAltContactNumber()))
            oldUser.setAltContactNumber(user.getAltContactNumber());
        if (!isEmpty(user.getBloodGroup()))
            oldUser.setBloodGroup(toEnumType(BloodGroup.class, user.getBloodGroup()));
        if (!isEmpty(user.getDob()))
            oldUser.setDob(user.getDob());
        if (!isEmpty(user.getEmailId()))
            oldUser.setEmailId(user.getEmailId());
        if (!isEmpty(user.getGender()))
            oldUser.setGender(toEnumType(Gender.class, user.getGender()));
        if (!isEmpty(user.getGuardian()))
            oldUser.setGuardian(user.getGuardian());
        if (!isEmpty(user.getGuardianRelation()))
            oldUser.setGuardianRelation(toEnumType(GuardianRelation.class, user.getGuardianRelation()));
        if (!isEmpty(user.getIdentificationMark()))
            oldUser.setIdentificationMark(user.getIdentificationMark());
        if (!isEmpty(user.getLocale()))
            oldUser.setLocale(user.getLocale());
        if (!isEmpty(user.getMobileNumber()))
            oldUser.setMobileNumber(user.getMobileNumber());
        if (!isEmpty(user.getName()))
            oldUser.setName(user.getName());
        if (!isEmpty(user.getPan()))
            oldUser.setPan(user.getPan());
        if (!isEmpty(user.getPassword()))
            oldUser.setPassword(user.getPassword());
        if (!isEmpty(user.getPhoto()))
            oldUser.setPhoto(user.getPhoto());
        if (!isEmpty(user.getPwdExpiryDate()))
            oldUser.setPwdExpiryDate(user.getPwdExpiryDate());
        if (!isEmpty(user.getRoles()))
            oldUser.setRoles(user.getRoles().stream().map(Role::new).collect(Collectors.toSet()));
        if (!isEmpty(user.getSalutation()))
            oldUser.setSalutation(user.getSalutation());
        if (!isEmpty(user.getSignature()))
            oldUser.setSignature(user.getSignature());
        if (!isEmpty(user.getTitle()))
            oldUser.setTitle(user.getTitle());
        if (!isEmpty(user.getType()))
            oldUser.setType(toEnumType(UserType.class, user.getType()));
        
        setEnrichedRolesToUser(oldUser);
        encryptPassword(oldUser);
        return userJpaRepository.save(oldUser).toDomain();
    }
}
