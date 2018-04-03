package org.egov.user.persistence.repository;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.user.domain.exception.InvalidRoleCodeException;
import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.Role;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.domain.model.enums.AddressType;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.GuardianRelation;
import org.egov.user.domain.model.enums.UserType;
import org.egov.user.repository.builder.RoleQueryBuilder;
import org.egov.user.repository.builder.UserTypeQueryBuilder;
import org.egov.user.repository.rowmapper.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class UserRepository {
	private static final String SELECT_NEXT_SEQUENCE = "select nextval('seq_eg_user')";

	private PasswordEncoder passwordEncoder;
	private AddressRepository addressRepository;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	private UserTypeQueryBuilder userTypeQueryBuilder;
	private RoleRepository roleRepository;

	public UserRepository(RoleRepository roleRepository, UserTypeQueryBuilder userTypeQueryBuilder,
			PasswordEncoder passwordEncoder, AddressRepository addressRepository, JdbcTemplate jdbcTemplate,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.passwordEncoder = passwordEncoder;
		this.addressRepository = addressRepository;
		this.roleRepository = roleRepository;
		this.userTypeQueryBuilder = userTypeQueryBuilder;
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * Get User By UserName And tenantId
	 * 
	 * @param userName
	 * @param tenantId
	 * @return
	 */
	public User findByUsername(String userName, String tenantId) {
		Map<String, Object> userInputs = new HashMap<String, Object>();
		userInputs.put("userName", userName);
		userInputs.put("tenantId", tenantId);
		User entityUser = null;
		UserRowMapper userRowMapper = new UserRowMapper();
		namedParameterJdbcTemplate.query(userTypeQueryBuilder.getFindUserByUserNameAndTenantId(), userInputs,
				userRowMapper);
		List<User> userList = userRowMapper.userList;
		if (userList != null && !userList.isEmpty()) {
			entityUser = userList.get(0);
			getRolesAndMap(entityUser);

		}
		return entityUser;
	}
	
	public User findByUsernameAndTenantId(String userName,String tenantId) {
		Map<String, Object> userInputs = new HashMap<String, Object>();
		userInputs.put("userName", userName);
		User entityUser = null;
		UserRowMapper userRowMapper = new UserRowMapper();
		namedParameterJdbcTemplate.query(userTypeQueryBuilder.getUserByUserNameAndTenantId(tenantId), userInputs,
				userRowMapper);
		List<User> userList = userRowMapper.userList;
		if (userList != null && !userList.isEmpty()) {
			entityUser = userList.get(0);
			getRolesAndMap(entityUser);

		}
		return entityUser;
	}

	/**
	 * api will check user is present or not with userName and id And tenantId
	 * 
	 * @param userName
	 * @param id
	 * @param tenantId
	 * @return
	 */
	public boolean isUserPresent(String userName, Long id, String tenantId) {

		String Query = userTypeQueryBuilder.getUserPresentByIdAndUserNameAndTenant();

		final Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("userName", userName);
		parametersMap.put("tenantId", tenantId);
		parametersMap.put("id", id);

		SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(Query, parametersMap);

		if (sqlRowSet.next() && sqlRowSet.getLong("id") > 0) {

			return true;
		}

		return false;

	}

	/**
	 * pi will check user is present or not with userName And tenantId
	 * 
	 * @param userName
	 * @param tenantId
	 * @return
	 */
	public boolean isUserPresent(String userName, String tenantId) {

		String Query = userTypeQueryBuilder.getUserPresentByUserNameAndTenant();

		final Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("userName", userName);
		parametersMap.put("tenantId", tenantId);

		SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(Query, parametersMap);

		if (sqlRowSet.next() && sqlRowSet.getLong("id") > 0) {

			return true;
		}

		return false;
	}

	/**
	 * api will fetch the user by emailId and TenantId
	 * 
	 * @param emailId
	 * @param tenantId
	 * @return
	 */
	public User findByEmailId(String emailId, String tenantId) {
		User entityUser = null;
		Map<String, Object> userInputs = new HashMap<String, Object>();
		userInputs.put("emailId", emailId);
		userInputs.put("tenantId", tenantId);
		UserRowMapper userRowMapper = new UserRowMapper();
		namedParameterJdbcTemplate.query(userTypeQueryBuilder.getUserByEmailAntTenant(), userInputs, userRowMapper);
		List<User> userList = userRowMapper.userList;
		if (userList != null && !userList.isEmpty()) {
			entityUser = userList.get(0);
			getRolesAndMap(entityUser);
		}
		return entityUser;
	}

	/**
	 * this api will create the user.
	 * 
	 * @param user
	 * @return
	 */
	public User create(User user) {
		setEnrichedRolesToUser(user);
		if (null != user.getPassword())
			encryptPassword(user);
		else
			user.setPassword(UUID.randomUUID().toString());
		final Long newId = getNextSequence();
		user.setId(newId);
		user.setCreatedDate(new Date());
		user.setLastModifiedDate(new Date());
		user.setCreatedBy(user.getLoggedInUserId());
		user.setLastModifiedBy(user.getLoggedInUserId());
		final User savedUser = save(user);
		if (user.getRoles().size() > 0) {
			saveUserRoles(user);
		}
		final Address savedCorrespondenceAddress = saveAddress(user.getCorrespondenceAddress(), savedUser.getId(),
				savedUser.getTenantId());
		final Address savedPermanentAddress = saveAddress(user.getPermanentAddress(), savedUser.getId(),
				savedUser.getTenantId());
		savedUser.setPermanentAddress(savedPermanentAddress);
		savedUser.setCorrespondenceAddress(savedCorrespondenceAddress);
		return savedUser;
	}

	/**
	 * api will do the mapping between user and role.
	 * 
	 * @param entityUser
	 */
	private void saveUserRoles(User entityUser) {
		List<Map<String, Object>> batchValues = new ArrayList<>(entityUser.getRoles().size());

		for (Role role : entityUser.getRoles()) {
			batchValues.add(
					new MapSqlParameterSource("roleid", role.getId()).addValue("roleidtenantid", role.getTenantId())
							.addValue("userid", entityUser.getId()).addValue("tenantid", entityUser.getTenantId())
							.addValue("lastmodifieddate", new Date()).getValues());
		}
		namedParameterJdbcTemplate.batchUpdate(RoleQueryBuilder.INSERT_USER_ROLES,
				batchValues.toArray(new Map[entityUser.getRoles().size()]));
	}

	/**
	 * api will persist the user.
	 * 
	 * @param entityUser
	 * @return
	 */
	private User save(User entityUser) {

		Map<String, Object> userInputs = new HashMap<String, Object>();

		userInputs.put("id", entityUser.getId());
		userInputs.put("uuid", entityUser.getUuid());
		userInputs.put("tenantid", entityUser.getTenantId());
		userInputs.put("salutation", entityUser.getSalutation());
		userInputs.put("dob", entityUser.getDob());
		userInputs.put("locale", entityUser.getLocale());
		userInputs.put("username", entityUser.getUsername());
		userInputs.put("password", entityUser.getPassword());
		userInputs.put("pwdexpirydate", entityUser.getPasswordExpiryDate());
		userInputs.put("mobilenumber", entityUser.getMobileNumber());
		userInputs.put("altcontactnumber", entityUser.getAltContactNumber());
		userInputs.put("emailid", entityUser.getEmailId());
		userInputs.put("active", entityUser.getActive());
		userInputs.put("name", entityUser.getName());
		if (Gender.FEMALE.equals(entityUser.getGender())) {
			userInputs.put("gender", 1);
		} else if (Gender.MALE.equals(entityUser.getGender())) {
			userInputs.put("gender", 2);
		} else if (Gender.OTHERS.equals(entityUser.getGender())) {
			userInputs.put("gender", 3);
		} else {
			userInputs.put("gender", 0);
		}

		userInputs.put("pan", entityUser.getPan());
		userInputs.put("aadhaarnumber", entityUser.getAadhaarNumber());
		if (UserType.BUSINESS.equals(entityUser.getType())) {
			userInputs.put("type", entityUser.getType().toString());
		} else if (UserType.CITIZEN.equals(entityUser.getType())) {
			userInputs.put("type", entityUser.getType().toString());
		} else if (UserType.EMPLOYEE.equals(entityUser.getType())) {
			userInputs.put("type", entityUser.getType().toString());
		} else if (UserType.SYSTEM.equals(entityUser.getType())) {
			userInputs.put("type", entityUser.getType().toString());
		} else {
			userInputs.put("type", "");
		}

		userInputs.put("guardian", entityUser.getGuardian());
		if (GuardianRelation.Father.equals(entityUser.getGuardianRelation())) {
			userInputs.put("guardianrelation", entityUser.getGuardianRelation().toString());
		} else if (GuardianRelation.Mother.equals(entityUser.getGuardianRelation())) {
			userInputs.put("guardianrelation", entityUser.getGuardianRelation().toString());
		} else if (GuardianRelation.Husband.equals(entityUser.getGuardianRelation())) {
			userInputs.put("guardianrelation", entityUser.getGuardianRelation().toString());
		} else if (GuardianRelation.Other.equals(entityUser.getGuardianRelation())) {
			userInputs.put("guardianrelation", entityUser.getGuardianRelation().toString());
		} else {
			userInputs.put("guardianrelation", "");
		}
		userInputs.put("signature", entityUser.getSignature());
		userInputs.put("accountlocked", entityUser.getAccountLocked());
		if (BloodGroup.A_NEGATIVE.equals(entityUser.getBloodGroup())) {
			userInputs.put("bloodgroup", entityUser.getBloodGroup().toString());
		} else if (BloodGroup.A_POSITIVE.equals(entityUser.getBloodGroup())) {
			userInputs.put("bloodgroup", entityUser.getBloodGroup().toString());
		} else if (BloodGroup.AB_NEGATIVE.equals(entityUser.getBloodGroup())) {
			userInputs.put("bloodgroup", entityUser.getBloodGroup().toString());
		} else if (BloodGroup.AB_POSITIVE.equals(entityUser.getBloodGroup())) {
			userInputs.put("bloodgroup", entityUser.getBloodGroup().toString());
		} else if (BloodGroup.O_NEGATIVE.equals(entityUser.getBloodGroup())) {
			userInputs.put("bloodgroup", entityUser.getBloodGroup().toString());
		} else if (BloodGroup.O_POSITIVE.equals(entityUser.getBloodGroup())) {
			userInputs.put("bloodgroup", entityUser.getBloodGroup().toString());
		} else {
			userInputs.put("bloodgroup", "");
		}
		userInputs.put("photo", entityUser.getPhoto());
		userInputs.put("identificationmark", entityUser.getIdentificationMark());
		userInputs.put("createddate", entityUser.getCreatedDate());
		userInputs.put("lastmodifieddate", entityUser.getLastModifiedDate());
		userInputs.put("createdby", entityUser.getLoggedInUserId());
		userInputs.put("lastmodifiedby", entityUser.getLoggedInUserId());

		namedParameterJdbcTemplate.update(userTypeQueryBuilder.getInsertUserQuery(), userInputs);
		return entityUser;
	}

	/**
	 * This api will return the next generate sequence of eg_user
	 * 
	 * @return
	 */
	private Long getNextSequence() {
		Long id = jdbcTemplate.queryForObject(SELECT_NEXT_SEQUENCE, Long.class);
		return id;
	}

	/**
	 * This api will save addresses for particular user.
	 * 
	 * @param address
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	private Address saveAddress(Address address, Long userId, String tenantId) {
		if (address != null) {
			addressRepository.create(address, userId, tenantId);
			return address;
		}
		return null;
	}

	/**
	 * api will get the all users by userSearchCriteria.After that roles and
	 * address are set in to the user object.
	 * 
	 * @param userSearch
	 * @return
	 */
	public List<User> findAll(UserSearchCriteria userSearch) {
		List<User> userEntities = findAllUsers(userSearch);
		userEntities.stream().map(this::getRolesAndMap).collect(Collectors.toList());
		return userEntities.stream().map(this::getAddressAndMapToDomain).collect(Collectors.toList());
	}

	/**
	 * api will get the user roles and set it to the user.
	 * 
	 * @param user
	 * @return
	 */
	private User getRolesAndMap(User user) {
		List<Role> roles = roleRepository.getUserRoles(user.getId(), user.getTenantId());
		user.setRoles(roles);
		return user;
	}

	/**
	 * api will fetch all users based on userSearch criteria.
	 * 
	 * @param userSearch
	 * @return
	 */
	private List<User> findAllUsers(UserSearchCriteria userSearch) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = userTypeQueryBuilder.getQuery(userSearch, preparedStatementValues);
		UserRowMapper rowMapper = new UserRowMapper();
		jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), rowMapper);
		final List<User> userList = rowMapper.userList;
		return userList;
	}

	/**
	 * api will get the addresses and map into the user object.
	 * 
	 * @param user
	 * @return
	 */
	private User getAddressAndMapToDomain(User user) {
		final List<Address> addresses = addressRepository.find(user.getId(), user.getTenantId());
		final Address correspondenceAddress = filter(addresses, AddressType.CORRESPONDENCE);
		final Address permanentAddress = filter(addresses, AddressType.PERMANENT);
		user.setCorrespondenceAddress(correspondenceAddress);
		user.setPermanentAddress(permanentAddress);
		return user;
	}

	/**
	 * api will filter the address by address type (Address Type can be
	 * PERMANANT , CORROSPONDANCE)
	 * 
	 * @param addresses
	 * @param addressType
	 * @return
	 */
	private Address filter(List<Address> addresses, AddressType addressType) {
		if (addresses == null) {
			return null;
		}
		return addresses.stream().filter(address -> addressType.toString().equals(address.getAddressType())).findFirst()
				.orElse(null);
	}

	/**
	 * api will encode the password.
	 * 
	 * @param entityUser
	 */
	private void encryptPassword(User entityUser) {
		final String encodedPassword = passwordEncoder.encode(entityUser.getPassword());
		entityUser.setPassword(encodedPassword);
	}

	/**
	 * Get the list of roles by user role code.
	 * 
	 * @param user
	 * @return
	 */
	private List<Role> fetchRolesByCode(User user) {
		return user.getRoles().stream().map((role) -> fetchRole(user, role)).collect(Collectors.toList());
	}

	/**
	 * Get the role based on user role code and tenantId.
	 * 
	 * @param user
	 * @param role
	 * @return
	 */
	private Role fetchRole(User user, Role role) {
		final Role enrichedRole = roleRepository.findByTenantIdAndCode(user.getTenantId(), role.getCode());
		if (enrichedRole == null) {
			throw new InvalidRoleCodeException(role.getCode());
		}
		return enrichedRole;
	}

	/**
	 * api will get the roles by user role codes and roles set it back to user
	 * object.
	 * 
	 * @param user
	 */
	private void setEnrichedRolesToUser(User user) {
		user.setRoles(fetchRolesByCode(user));
	}

	/**
	 * api will get the user Details by id And tenantId
	 * 
	 * @param id
	 * @param tenantId
	 * @return
	 */
	public User getUserById(final Long id, String tenantId) {
		User entityUser = getUserByIdAndTenantId(id, tenantId);
		if (entityUser != null) {
			getAddressAndMapToDomain(entityUser);
		}
		return entityUser != null ? entityUser : null;
	}

	/**
	 * api will update the user details.
	 * 
	 * @param user
	 * @return
	 */
	public User update(final User user) {

		User oldUser = getUserByIdAndTenantId(user.getId(), user.getTenantId());

		Map<String, Object> updateuserInputs = new HashMap<String, Object>();

		updateuserInputs.put("id", user.getId());
		updateuserInputs.put("tenantid", user.getTenantId());
		updateuserInputs.put("AadhaarNumber", user.getAadhaarNumber());
		updateuserInputs.put("AccountLocked", user.getAccountLocked());
		updateuserInputs.put("Active", user.getActive());
		updateuserInputs.put("AltContactNumber", user.getAltContactNumber());

		if (user.getBloodGroup() != null) {
			if (BloodGroup.A_NEGATIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.A_POSITIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.AB_NEGATIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.AB_POSITIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.O_NEGATIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.O_POSITIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.B_POSITIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else if (BloodGroup.B_NEGATIVE.toString().equals(user.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", user.getBloodGroup().toString());
			} else {
				updateuserInputs.put("BloodGroup", "");
			}
		} else if (oldUser!=null && oldUser.getBloodGroup() != null) {
			if (BloodGroup.A_NEGATIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.A_POSITIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.AB_NEGATIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.AB_POSITIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.O_NEGATIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.O_POSITIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.B_POSITIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else if (BloodGroup.B_NEGATIVE.toString().equals(oldUser.getBloodGroup().toString())) {
				updateuserInputs.put("BloodGroup", oldUser.getBloodGroup().toString());
			} else {
				updateuserInputs.put("BloodGroup", "");
			}
		} else {
			updateuserInputs.put("BloodGroup", "");
		}

		if (user.getDob() != null) {
			updateuserInputs.put("Dob", user.getDob());
		} else {
			updateuserInputs.put("Dob", oldUser.getDob());
		}
		updateuserInputs.put("EmailId", user.getEmailId());

		if (user.getGender() != null) {
			if (Gender.FEMALE.toString().equals(user.getGender().toString())) {
				updateuserInputs.put("Gender", 1);
			} else if (Gender.MALE.toString().equals(user.getGender().toString())) {
				updateuserInputs.put("Gender", 2);
			} else if (Gender.OTHERS.toString().equals(user.getGender().toString())) {
				updateuserInputs.put("Gender", 3);
			} else {
				updateuserInputs.put("Gender", 0);
			}
		} else {
			updateuserInputs.put("Gender", 0);
		}
		updateuserInputs.put("Guardian", user.getGuardian());

		if (user.getGuardianRelation() != null) {
			if (GuardianRelation.Father.toString().equals(user.getGuardianRelation().toString())) {
				updateuserInputs.put("GuardianRelation", user.getGuardianRelation().toString());
			} else if (GuardianRelation.Mother.toString().equals(user.getGuardianRelation().toString())) {
				updateuserInputs.put("GuardianRelation", user.getGuardianRelation().toString());
			} else if (GuardianRelation.Husband.toString().equals(user.getGuardianRelation().toString())) {
				updateuserInputs.put("GuardianRelation", user.getGuardianRelation().toString());
			} else if (GuardianRelation.Other.toString().equals(user.getGuardianRelation().toString())) {
				updateuserInputs.put("GuardianRelation", user.getGuardianRelation().toString());
			} else {
				updateuserInputs.put("GuardianRelation", "");
			}
		} else {
			updateuserInputs.put("GuardianRelation", "");
		}
		updateuserInputs.put("IdentificationMark", user.getIdentificationMark());
		updateuserInputs.put("Locale", user.getLocale());
		if (null != user.getMobileNumber())
			updateuserInputs.put("MobileNumber", user.getMobileNumber());
		else
			updateuserInputs.put("MobileNumber", oldUser.getMobileNumber());
		updateuserInputs.put("Name", user.getName());
		updateuserInputs.put("Pan", user.getPan());

		if (!isEmpty(user.getPassword()))
			updateuserInputs.put("Password", passwordEncoder.encode(user.getPassword()));
		else
			updateuserInputs.put("Password", oldUser.getPassword());
       if(oldUser!=null && user.getPhoto()!=null && user.getPhoto().contains("http"))
    	   updateuserInputs.put("Photo", oldUser.getPhoto());
		updateuserInputs.put("Photo", user.getPhoto());
		if (null != user.getPasswordExpiryDate())
			updateuserInputs.put("PasswordExpiryDate", user.getPasswordExpiryDate());
		else
			updateuserInputs.put("PasswordExpiryDate", oldUser.getPasswordExpiryDate());
		updateuserInputs.put("Salutation", user.getSalutation());
		updateuserInputs.put("Signature", user.getSignature());
		updateuserInputs.put("Title", user.getTitle());

		if (user.getType() != null) {
			if (UserType.BUSINESS.toString().equals(user.getType().toString())) {
				updateuserInputs.put("Type", user.getType().toString());
			} else if (UserType.CITIZEN.toString().equals(user.getType().toString())) {
				updateuserInputs.put("Type", user.getType().toString());
			} else if (UserType.EMPLOYEE.toString().equals(user.getType().toString())) {
				updateuserInputs.put("Type", user.getType().toString());
			} else if (UserType.SYSTEM.toString().equals(user.getType().toString())) {
				updateuserInputs.put("Type", user.getType().toString());
			} else {
				updateuserInputs.put("Type", "");
			}
		} else {
			updateuserInputs.put("Type", oldUser.getType().toString());
		}
		updateuserInputs.put("LastModifiedDate", new Date());
		updateuserInputs.put("LastModifiedBy", 1);

		namedParameterJdbcTemplate.update(userTypeQueryBuilder.getUpdateUserQuery(), updateuserInputs);
		if (user.getRoles() != null && !CollectionUtils.isEmpty(user.getRoles())) {
			setEnrichedRolesToUser(user);
			updateRoles(user);
		}
		if (user.getAddresses() != null) {
			addressRepository.update(user.getAddresses(), user.getId(), user.getTenantId());
		}
		User updateduser = getUserByIdAndTenantId(user.getId(), user.getTenantId());
		return getAddressAndMapToDomain(updateduser);
	}

	/**
	 * api will update the user Roles.
	 * 
	 * @param user
	 */
	private void updateRoles(User user) {
		Map<String, Object> roleInputs = new HashMap<String, Object>();
		roleInputs.put("userId", user.getId());
		roleInputs.put("tenantId", user.getTenantId());
		namedParameterJdbcTemplate.update(RoleQueryBuilder.DELETE_USER_ROLES, roleInputs);
		saveUserRoles(user);
	}

	/**
	 * Get the user By userId and tenantId
	 * 
	 * @param id
	 * @param tenantId
	 * @return
	 */
	public User getUserByIdAndTenantId(Long id, String tenantId) {

		User entityUser = null;
		Map<String, Object> userInputs = new HashMap<String, Object>();
		userInputs.put("id", id);
		userInputs.put("tenantId", tenantId);
		UserRowMapper userRowMapper = new UserRowMapper();
		namedParameterJdbcTemplate.query(userTypeQueryBuilder.getFindUserByIdAndTenantId(), userInputs, userRowMapper);
		List<User> userList = userRowMapper.userList;
		if (userList != null && !userList.isEmpty()) {
			entityUser = userList.get(0);
			entityUser.setRoles(roleRepository.getUserRoles(id, tenantId));
		}

		return entityUser;
	}
}
