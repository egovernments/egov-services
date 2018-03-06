package org.egov.user.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.enums.AddressType;
import org.egov.user.domain.model.enums.UserType;
import org.egov.user.domain.v11.model.AuditDetails;
import org.egov.user.domain.v11.model.Role;
import org.egov.user.domain.v11.model.User;
import org.egov.user.domain.v11.model.UserDetails;
import org.egov.user.domain.v11.model.UserSearchCriteria;
import org.egov.user.persistence.repository.UserRepository;
import org.egov.user.producer.UserProducer;
import org.egov.user.v11.repository.IdGenRepository;
import org.egov.user.v11.repository.SearcherRepository;
import org.egov.user.web.v11.contract.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceVersionv11 {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IdGenRepository idGenRepository;

	@Autowired
	UserProducer userProducer;

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
		RequestInfo requestInfo = createUserRequest.getRequestInfo();
		if (null == requestInfo) {
			List<User> citizenUsers = createUserRequest.getUsers();
			for (User user : citizenUsers) {
				if (!user.getType().equals(UserType.CITIZEN.toString())) {
					throw new CustomException("400", "Users Type should be CITIZEN");
				}
				user.setRoleToCitizen();
			}
			setRequiredInfoToUsers(createUserRequest);
			userProducer.producer(saveUserTopic, createUserRequest);

		} else if (null != requestInfo && null != requestInfo.getUserInfo()
				&& null != requestInfo.getUserInfo().getType()) {

			String userType = requestInfo.getUserInfo().getType();
			if (userType.equalsIgnoreCase(UserType.SYSTEM.toString())
					|| userType.equalsIgnoreCase(UserType.EMPLOYEE.toString())) {
				for (User user : users) {
					if (user.getType().equalsIgnoreCase(UserType.CITIZEN.toString())) {
						user.setRoleToCitizen();
					}
				}
				setRequiredInfoToUsers(createUserRequest);
				userProducer.producer(saveUserTopic, createUserRequest);
			}
		}
	}

	public void updateUser(UserRequest createUserRequest) {
		List<User> userList = createUserRequest.getUsers();
		String username = createUserRequest.getRequestInfo().getUserInfo().getName();
		AuditDetails auditDetails = AuditDetails.builder().createdBy(username).lastModifiedBy(username)
				.createdTime(new Date().getTime()).lastModifiedTime(new Date().getTime()).build();
		userList.forEach(user -> user.setAuditDetails(auditDetails));
		for (User user : userList) {
			List<Role> roles = user.getRoles();
			if (null != roles) {
				roles.forEach(role -> role.setTenantId(user.getTenantId()));
				roles.forEach(role -> role.setUserId(user.getId()));
				roles.forEach(role -> role.setLastModifiedDate(new Date().getTime()));
			}
		}

		userList = prepareUserAddress(userList);
		userProducer.producer(updateUserTopic, createUserRequest);
	}

	public boolean isUserPresent(String userName, String tenantId) {
		// TODO Auto-generated method stub
		return userRepository.isUserPresent(userName, tenantId);
	}

	private void setRequiredInfoToUsers(UserRequest createUserRequest) {
		// TODO Auto-generated method stub
		List<Long> idList = getIds(createUserRequest.getUsers().size(), userSequenceName);
		List<User> userList = createUserRequest.getUsers();
		if (null != idList) {
			for (int i = 0; i < userList.size(); i++) {
				userList.get(i).setId(idList.get(i));
			}
		}
		String username = createUserRequest.getRequestInfo().getUserInfo().getName();
		AuditDetails auditDetails = AuditDetails.builder().createdBy(username).lastModifiedBy(username)
				.createdTime(new Date().getTime()).lastModifiedTime(new Date().getTime()).build();
		userList.forEach(user -> user.setAuditDetails(auditDetails));

		for (User user : userList) {
			List<Role> roles = user.getRoles();
			if (null != roles) {
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
				if (null != userDetails.getPermanentAddress() || null != userDetails.getPermanentCity()
						|| null != userDetails.getPermanentPincode()) {
					Address permentAddress = Address.builder().addressType(AddressType.PERMANENT.toString())
							.address(userDetails.getPermanentAddress()).city(userDetails.getPermanentCity())
							.pinCode(userDetails.getPermanentPincode()).userId(user.getId())
							.tenantId(user.getTenantId()).build();
					addressList.add(permentAddress);
				}
				if (null != userDetails.getCorrespondenceAddress() || null != userDetails.getCorrespondenceCity()
						|| null != userDetails.getCorrespondencePincode()) {
					Address correspondenceAddress = Address.builder().addressType(AddressType.CORRESPONDENCE.toString())
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

		if (null != searchCriteria.getIncludeDetails() && searchCriteria.getIncludeDetails()) {
			for (User user : userList) {
				if (null != user.getUserDetails()) {
					if (null != user.getUserDetails().getAddresses()) {
						List<Address> addressList = user.getUserDetails().getAddresses();
						for (Address address : addressList) {
							if (address.getAddressType().equals(AddressType.PERMANENT.toString())) {
								user.getUserDetails().setPermanentAddress(address.getAddress());
								user.getUserDetails().setPermanentCity(address.getCity());
								user.getUserDetails().setPermanentPincode(address.getPinCode());
							} else if (address.getAddressType().equals(AddressType.CORRESPONDENCE.toString())) {
								user.getUserDetails().setCorrespondenceAddress(address.getAddress());
								user.getUserDetails().setCorrespondenceCity(address.getCity());
								user.getUserDetails().setCorrespondencePincode(address.getPinCode());
							}
						}
					}
				}
			}
		} else if (null == searchCriteria.getIncludeDetails() || searchCriteria.getIncludeDetails() == false) {
			for (User user : userList) {
				user.setUserDetails(null);
			}
		}
		List<String> roleCodes = searchCriteria.getRoleCodes();
		if (null != roleCodes) {
			List<User> users = new ArrayList<User>();
			for (User user : userList) {
				List<Role> roles = user.getRoles();
				if (null != roles)
					roles = roles.stream().filter(i -> roleCodes.contains(i.getCode())).collect(Collectors.toList());
				if (null != roles && roles.size() > 0) {
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
		userProducer.producer(updateUserOtpTopic, user);

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("tenantId", tenantId);
		hashMap.put("otp", otpNumber);
		hashMap.put("identity", mobileNumber);
		hashMap.put("auditDetails", auditDetails);
		userProducer.producer(userSmsNotification, hashMap);
	}
}
