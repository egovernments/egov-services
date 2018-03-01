package org.egov.user.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.user.domain.model.Address;
import org.egov.user.domain.v11.model.AuditDetails;
import org.egov.user.domain.v11.model.Role;
import org.egov.user.domain.v11.model.User;
import org.egov.user.domain.v11.model.UserDetails;
import org.egov.user.domain.v11.model.UserSearchCriteria;
import org.egov.user.persistence.repository.UserRepository;
import org.egov.user.v11.repository.IdGenRepository;
import org.egov.user.v11.repository.SearcherRepository;
import org.egov.user.web.v11.contract.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceVersionv11 {

	@Autowired
	LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IdGenRepository idGenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SearcherRepository searchRepository;

	@Value("${kafka.save.user.topic}")
	private String saveUserTopic;

	@Value("${kafka.update.user.topic}")
	private String updateUserTopic;

	@Value("${kafka.update.userotp.topic}")
	private String updateUserOtpTopic;

	@Value("${password.expiry.in.days}")
	int PasswordExpiryInDays;

	@Value("${egov.user.sequence.name}")
	private String userSequenceName;

	@Value("${kafka.topics.notification.user.otp}")
	private String userSmsNotification;

	public void createUser(UserRequest createUserRequest) {
		List<User> users = createUserRequest.getUsers();
		for (User user : users) {
			user.setDefaultPasswordExpiry(PasswordExpiryInDays);
		}
		String userType = createUserRequest.getRequestInfo().getUserInfo().getType();
		if (userType == null) {
			List<User> citizenUsers = createUserRequest.getUsers();
			for (User user : citizenUsers) {
				if (!user.getType().equals("CITIZEN")) {
					throw new CustomException("400", "Users Type should be CITIZEN");
				}
				user.setRoleToCitizen();
			}
			setRequiredInfoToUsers(createUserRequest);
			kafkaTemplate.send(saveUserTopic, createUserRequest);

		} else if (userType.equalsIgnoreCase("SYSTEM") || userType.equalsIgnoreCase("EMPLOYEE")) {
			for (User user : users) {
				if (user.getType().equalsIgnoreCase("CITIZEN")) {
					user.setRoleToCitizen();
				}
			}
			setRequiredInfoToUsers(createUserRequest);
			kafkaTemplate.send(saveUserTopic, createUserRequest);
		}
	}

	public void updateUser(UserRequest createUserRequest) {
		kafkaTemplate.send(updateUserTopic, createUserRequest);
	}

	private void setUserInfoFromDbuser(User user, User dbuser) {
		// TODO Auto-generated method stub
		if (user.getName() == null)
			user.setName(dbuser.getName());
		if (user.getAadhaarNumber() == null)
			user.setAadhaarNumber(dbuser.getAadhaarNumber());
		if (user.getMobileNumber() == null)
			user.setMobileNumber(dbuser.getMobileNumber());
		if (user.getActive() == null)
			user.setActive(dbuser.getActive());
		if (user.getAccountLocked() == null)
			user.setAccountLocked(dbuser.getAccountLocked());
		if (user.getEmailId() == null)
			user.setEmailId(dbuser.getEmailId());
		if (user.getGender() == null)
			user.setGender(dbuser.getGender());
		if (user.getLocale() == null)
			user.setLocale(dbuser.getLocale());
		if (user.getType() == null)
			user.setType(dbuser.getType());
		if (user.getUserDetails() == null)
			user.setUserDetails(dbuser.getUserDetails());
		if (user.getRoles() == null)
			user.setRoles(dbuser.getRoles());
		AuditDetails auditDetails = dbuser.getAuditDetails();
		auditDetails.setLastModifiedTime(new Date().getTime());
		user.setAuditDetails(auditDetails);
	}

	public boolean isUserPresent(String userName, String tenantId) {
		// TODO Auto-generated method stub
		return userRepository.isUserPresent(userName, tenantId);
	}

	private void setRequiredInfoToUsers(UserRequest createUserRequest) {
		// TODO Auto-generated method stub
		List<Long> idList = getIds(createUserRequest.getUsers().size(), userSequenceName);
		List<User> userList = createUserRequest.getUsers();
		for (int i = 0; i < userList.size(); i++) {
			userList.get(i).setId(idList.get(i));
		}
		String username = createUserRequest.getRequestInfo().getUserInfo().getName();
		AuditDetails auditDetails = AuditDetails.builder().createdBy(username).lastModifiedBy(username)
				.createdTime(new Date().getTime()).lastModifiedTime(new Date().getTime()).build();
		userList.forEach(user -> user.setAuditDetails(auditDetails));

		for (User user : userList) {
			List<Role> roles = user.getRoles();
			if (roles != null) {
				roles.forEach(role -> role.setTenantId(user.getTenantId()));
				roles.forEach(role -> role.setUserId(user.getId()));
				roles.forEach(role -> role.setLastModifiedDate(new Date().getTime()));
			}
		}

		userList = prepareUserAddress(userList);
		createUserRequest.setUsers(userList);
	}

	private List<User> prepareUserAddress(List<User> userList) {
		// TODO Auto-generated method stub
		UserDetails userDetails = null;
		List<Address> addressList = null;
		for (User user : userList) {
			userDetails = user.getUserDetails();
			if (userDetails != null) {
				addressList = new ArrayList<Address>();
				if (userDetails.getPermanentAddress() != null || userDetails.getPermanentCity() != null
						|| userDetails.getPermanentPincode() != null) {
					Address permentAddress = Address.builder().addressType("PERMANENT")
							.address(userDetails.getPermanentAddress()).city(userDetails.getPermanentCity())
							.pinCode(userDetails.getPermanentPincode()).userId(user.getId())
							.tenantId(user.getTenantId()).build();
					addressList.add(permentAddress);
				}
				if (userDetails.getCorrespondenceAddress() != null || userDetails.getCorrespondenceCity() != null
						|| userDetails.getCorrespondencePincode() != null) {
					Address correspondenceAddress = Address.builder().addressType("CORRESPONDENCE")
							.address(userDetails.getCorrespondenceAddress()).city(userDetails.getCorrespondenceCity())
							.pinCode(userDetails.getCorrespondencePincode()).userId(user.getId())
							.tenantId(user.getTenantId()).build();
					addressList.add(correspondenceAddress);
				}
				userDetails.setAddresses(addressList);
			}
		}
		return userList;
	}

	public List<Long> getIds(int rsSize, String sequenceName) {

		String generateIdQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1,?)";
		List<Long> idList = null;
		try {
			idList = jdbcTemplate.queryForList(generateIdQuery, new Object[] { rsSize }, Long.class);
		} catch (Exception e) {
			log.error("the exception from sequence id gen : " + e);
			throw e;
		}
		return idList;
	}

	public List<User> searchUsers(RequestInfo requestInfo, UserSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		List<User> userList = searchRepository.getAllUsers(requestInfo, searchCriteria);

		if (searchCriteria.getIncludeDetails() != null && searchCriteria.getIncludeDetails()) {
			for (User user : userList) {
				if (user.getUserDetails() != null) {
					if (user.getUserDetails().getAddresses() != null) {
						List<Address> addressList = user.getUserDetails().getAddresses();
						for (Address address : addressList) {
							if (address.getAddressType().equals("PERMANENT")) {
								user.getUserDetails().setPermanentAddress(address.getAddress());
								user.getUserDetails().setPermanentCity(address.getCity());
								user.getUserDetails().setPermanentPincode(address.getPinCode());
							} else if (address.getAddressType().equals("CORRESPONDENCE")) {
								user.getUserDetails().setCorrespondenceAddress(address.getAddress());
								user.getUserDetails().setCorrespondenceCity(address.getCity());
								user.getUserDetails().setCorrespondencePincode(address.getPinCode());
							}
						}
					}
				}
			}
		} else if (searchCriteria.getIncludeDetails() == null || searchCriteria.getIncludeDetails() == false) {
			for (User user : userList) {
				user.setUserDetails(null);
			}
		}
		List<String> roleCodes = searchCriteria.getRoleCodes();
		if (roleCodes != null) {
			List<User> users = new ArrayList<User>();
			for (User user : userList) {
				List<Role> roles = user.getRoles();
				if (roles != null)
					roles = roles.stream().filter(i -> roleCodes.contains(i.getCode())).collect(Collectors.toList());
				if (roles != null && roles.size() > 0) {
					users.add(user);
				}
			}
			return users;
		}
		return userList;
	}

	public void createAndSendOtp(RequestInfo requestInfo, String tenantId, String userName, String mobileNumber) {
		// TODO Auto-generated method stub
		String otpNumber = idGenRepository.generateUserOtpNumber(requestInfo, tenantId);
		String createdBy = requestInfo.getUserInfo().getName();
		AuditDetails auditDetails = AuditDetails.builder().createdBy(createdBy).lastModifiedBy(createdBy)
				.createdTime(new Date().getTime()).lastModifiedTime(new Date().getTime()).build();
		User user = User.builder().mobileNumber(mobileNumber).userName(userName).password(otpNumber).tenantId(tenantId)
				.auditDetails(auditDetails).build();
		kafkaTemplate.send(updateUserOtpTopic, user);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("tenantId", tenantId);
		hashMap.put("otp", otpNumber);
		hashMap.put("identity", mobileNumber);
		hashMap.put("auditDetails", auditDetails);
		kafkaTemplate.send(userSmsNotification, hashMap);
	}
}
