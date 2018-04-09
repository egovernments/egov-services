package org.egov.user.persistence.repository;

import org.egov.user.domain.exception.InvalidRoleCodeException;
import org.egov.user.domain.model.UserSearchCriteria;
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
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.egov.user.persistence.entity.EnumConverter.toEnumType;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class UserRepository {
	private static final String SELECT_NEXT_SEQUENCE = "select nextval('seq_eg_user')";
	private UserJpaRepository userJpaRepository;
	private UserSearchSpecificationFactory userSearchSpecificationFactory;
	private RoleJpaRepository roleJpaRepository;
	private PasswordEncoder passwordEncoder;
	private AddressRepository addressRepository;
	private EntityManager entityManager;

	public UserRepository(UserJpaRepository userJpaRepository,
						  UserSearchSpecificationFactory userSearchSpecificationFactory,
						  RoleJpaRepository roleJpaRepository,
						  PasswordEncoder passwordEncoder,
						  AddressRepository addressRepository, EntityManager entityManager) {
		this.userJpaRepository = userJpaRepository;
		this.userSearchSpecificationFactory = userSearchSpecificationFactory;
		this.roleJpaRepository = roleJpaRepository;
		this.passwordEncoder = passwordEncoder;
		this.addressRepository = addressRepository;
		this.entityManager = entityManager;
	}

	public org.egov.user.domain.model.User findByUsername(String userName, String tenantId) {
		final User entityUser = userJpaRepository.findByUsernameAndUserKeyTenantId(userName, tenantId);
		return entityUser != null ? entityUser.toDomain(null, null) : null;
	}

	public boolean isUserPresent(String userName, Long id, String tenantId) {
		return userJpaRepository.isUserPresent(userName, id, tenantId) > 0;
	}

	public boolean isUserPresent(String userName, String tenantId) {
		return userJpaRepository.isUserPresent(userName, tenantId) > 0;
	}

	public org.egov.user.domain.model.User findByEmailId(String emailId, String tenantId) {
		final User entityUser = userJpaRepository.findByEmailIdAndUserKeyTenantId(emailId, tenantId);
		return entityUser != null ? entityUser.toDomain(null, null) : null;
	}

	public org.egov.user.domain.model.User create(org.egov.user.domain.model.User domainUser) {
		User entityUser = new User(domainUser);
		setEnrichedRolesToUser(entityUser);
		encryptPassword(entityUser);
		entityUser.setCreatedDate(new Date());
		entityUser.setLastModifiedDate(new Date());
		final Long newId = getNextSequence();
		entityUser.getUserKey().setId(newId);
		final User savedUser = userJpaRepository.save(entityUser);
		final org.egov.user.domain.model.Address savedCorrespondenceAddress =
				saveAddress(domainUser.getCorrespondenceAddress(), savedUser.getId().getId(),
						savedUser.getId().getTenantId());
		final org.egov.user.domain.model.Address savedPermanentAddress =
				saveAddress(domainUser.getPermanentAddress(), savedUser.getId().getId(), savedUser.getId().getTenantId());
		return savedUser.toDomain(savedCorrespondenceAddress, savedPermanentAddress);
	}

	private long getNextSequence() {
		final Query query = entityManager.createNativeQuery(SELECT_NEXT_SEQUENCE);
		return ((BigInteger) query.getSingleResult()).longValue();
	}

	private org.egov.user.domain.model.Address saveAddress(org.egov.user.domain.model.Address address,
														   Long userId,
														   String tenantId) {
		if (address != null) {
			addressRepository.create(address, userId, tenantId);
			return address;
		}
		return null;
	}

	public List<org.egov.user.domain.model.User> findAll(UserSearchCriteria userSearch) {
		Specification<User> specification = userSearchSpecificationFactory.getSpecification(userSearch);
		PageRequest pageRequest = createPageRequest(userSearch);
		List<User> userEntities = userJpaRepository.findAll(specification, pageRequest).getContent();
		return userEntities.stream().map(this::getAddressAndMapToDomain).collect(Collectors.toList());
	}

	private org.egov.user.domain.model.User getAddressAndMapToDomain(User user) {
		final List<org.egov.user.domain.model.Address> addresses = addressRepository.
				find(user.getId().getId(), user.getId().getTenantId());
		final org.egov.user.domain.model.Address correspondenceAddress = filter(addresses,
				org.egov.user.domain.model.enums.AddressType.CORRESPONDENCE);
		final org.egov.user.domain.model.Address permanentAddress =
				filter(addresses, org.egov.user.domain.model.enums.AddressType.PERMANENT);
		return user.toDomain(correspondenceAddress, permanentAddress);
	}

	private org.egov.user.domain.model.Address filter(List<org.egov.user.domain.model.Address> addresses,
													  org.egov.user.domain.model.enums.AddressType addressType) {
		if (addresses == null) {
			return null;
		}
		return addresses.stream()
				.filter(address -> addressType.equals(address.getType()))
				.findFirst()
				.orElse(null);
	}

	private void encryptPassword(User entityUser) {
		final String encodedPassword = passwordEncoder.encode(entityUser.getPassword());
		entityUser.setPassword(encodedPassword);
	}

	private PageRequest createPageRequest(UserSearchCriteria userSearch) {
		Sort sort = createSort(userSearch);
		return new PageRequest(userSearch.getPageNumber(), userSearch.getPageSize(), sort);
	}

	private Sort createSort(UserSearchCriteria userSearch) {
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

	private Set<Role> fetchRolesByCode(User user) {
		return user.getRoles()
				.stream()
				.map((role) -> fetchRole(user, role))
				.collect(Collectors.toSet());
	}

	private Role fetchRole(User user, Role role) {
		final Role enrichedRole = roleJpaRepository
				.findByRoleKeyTenantIdAndCodeIgnoreCase(user.getId().getTenantId(), role.getCode());
		if (enrichedRole == null) {
			throw new InvalidRoleCodeException(role.getCode());
		}
		return enrichedRole;
	}

	private void setEnrichedRolesToUser(User user) {
		final Set<Role> roles = fetchRolesByCode(user);
		user.setRoles(roles);
	}

	public org.egov.user.domain.model.User getUserById(final Long id, String tenantId) {
		final User entityUser = userJpaRepository.findByUserKeyIdAndUserKeyTenantId(id, tenantId);
		return entityUser != null ? entityUser.toDomain(null, null) : null;
	}

	public org.egov.user.domain.model.User update(final org.egov.user.domain.model.User user) {
		User oldUser = userJpaRepository.findByUserKeyIdAndUserKeyTenantId(user.getId(), user.getTenantId());
		oldUser.setAadhaarNumber(user.getAadhaarNumber());
		if(user.getAccountLocked() != null) {
			oldUser.setAccountLocked(user.getAccountLocked());
		}
		if(user.getActive() != null) {
			oldUser.setActive(user.getActive());
		}
		oldUser.setAltContactNumber(user.getAltContactNumber());
		oldUser.setBloodGroup(toEnumType(BloodGroup.class, user.getBloodGroup()));
		oldUser.setDob(user.getDob());
		oldUser.setEmailId(user.getEmailId());
		oldUser.setGender(toEnumType(Gender.class, user.getGender()));
		oldUser.setGuardian(user.getGuardian());
		oldUser.setGuardianRelation(toEnumType(GuardianRelation.class, user.getGuardianRelation()));
		oldUser.setIdentificationMark(user.getIdentificationMark());
		oldUser.setLocale(user.getLocale());
		if(!isEmpty(user.getMobileNumber())) {
			oldUser.setMobileNumber(user.getMobileNumber());
		}
		oldUser.setName(user.getName());
		oldUser.setPan(user.getPan());
		if(!isEmpty(user.getPassword())) {
			oldUser.setPassword(user.getPassword());
			encryptPassword(oldUser);
		}
		oldUser.setPhoto(user.getPhoto());
		if(user.getPasswordExpiryDate() != null) {
			oldUser.setPwdExpiryDate(user.getPasswordExpiryDate());
		}
		if(!CollectionUtils.isEmpty(user.getRoles())) {
			oldUser.setRoles(user.getRoles().stream().map(Role::new).collect(Collectors.toSet()));
		}
		oldUser.setSalutation(user.getSalutation());
		oldUser.setSignature(user.getSignature());
		oldUser.setTitle(user.getTitle());
		if(user.getType() != null) {
			oldUser.setType(toEnumType(UserType.class, user.getType()));
		}
		setEnrichedRolesToUser(oldUser);
		oldUser.setLastModifiedDate(new Date());
		addressRepository.update(user.getAddresses(), user.getId(), user.getTenantId());
		return userJpaRepository.save(oldUser).toDomain(user.getCorrespondenceAddress(), user.getPermanentAddress());
	}
}
