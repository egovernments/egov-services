package org.egov.user.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.user.domain.model.Otp;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.domain.model.enums.UserType;
import org.egov.user.producer.UserProducer;
import org.egov.user.repository.IdGenRepository;
import org.egov.user.repository.SearcherRepository;
import org.egov.user.web.contract.AuditDetails;
import org.egov.user.web.contract.Role;
import org.egov.user.web.contract.User;
import org.egov.user.web.contract.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IdGenRepository idGenRepository;

	@Autowired
	private UserProducer userProducer;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SearcherRepository searchRepository;

	@Value("${kafka.save.user.topic}")
	private String saveUserTopic;

	@Value("${kafka.update.user.topic}")
	private String updateUserTopic;

	@Value("${kafka.update.user.password.topic}")
	private String updateUserPasswordTopic;

	@Value("${password.expiry.in.days}")
	private int PasswordExpiryInDays;

	@Value("${egov.user.sequence.name}")
	private String userSequenceName;

	@Value("${kafka.topics.notification.user.otp}")
	private String userSmsNotification;

	@Value("${user.login.password.otp.enabled}")
	private boolean isUserLoginPasswordOtpEnabled;

	@Value("${kafka.save.user.otp.topic}")
	private String userSaveOtpTopic;

	@Value("${otp.validation.time.seconds}")
	private long totalTimeForOtpValidation;

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
					throw new CustomException(HttpStatus.BAD_REQUEST.toString(), "Users Type should be CITIZEN");
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
/*			if (null != user.getUserDetails()) {
				List<Address> addressList = user.getUserDetails().getAddresses();
				if (null != addressList && addressList.size() > 0) {
					for (Address address : addressList) {
						address.setUserId(user.getId());
						address.setTenantId(user.getTenantId());
					}
				}
			}*/
		}
		if (!isUserLoginPasswordOtpEnabled) {
			updatePassword(createUserRequest, userList.get(0));
		} else {
			userProducer.producer(updateUserTopic, createUserRequest);
		}
	}

	private void updatePassword(UserRequest createUserRequest, User user) {

		if (null != user.getPassword() && null != user.getOtpReference()) {
			Otp otp = Otp.builder().userId(user.getId()).otp(user.getOtpReference()).build();
			Otp otp1 = searchRepository.getOtpByUseridAndOtp(createUserRequest.getRequestInfo(), otp);
			System.out.println("otp1" + otp1);
			Long currentTime = System.currentTimeMillis() / 1000;
			if (null != otp1) {
				Long createdTime = otp1.getAuditDetails().getCreatedTime() / 1000;
				if (((currentTime - createdTime) <= totalTimeForOtpValidation)) {
					String encryptedPassword = passwordEncoder.encode(user.getPassword());
					user.setPassword(encryptedPassword);
					userProducer.producer(updateUserPasswordTopic, user);
				} else {
					throw new CustomException(HttpStatus.BAD_REQUEST.toString(),
							"Otp time Got Expired , Please Generate New Otp And Try UpdatePassword with new otp.");
				}
			} else {
				throw new CustomException(HttpStatus.BAD_REQUEST.toString(), "There is No Otp for this userId");
			}
		} else {
			userProducer.producer(updateUserTopic, createUserRequest);
		}
	}

	public boolean isUserPresent(String userName, String tenantId) {
		// TODO Auto-generated method stub
		RequestInfo requestInfo = RequestInfo.builder().build();
		UserSearchCriteria searchCriteria = UserSearchCriteria.builder().userName(userName).tenantId(tenantId).build();
		List<User> users = searchRepository.getAllUsers(requestInfo, searchCriteria);
		if (null != users && users.size() > 0) {
			return true;
		}
		return false;
	}

	private void setRequiredInfoToUsers(UserRequest createUserRequest) {
		// TODO Auto-generated method stub
		List<Long> idList = getIds(createUserRequest.getUsers().size(), userSequenceName);
		List<User> userList = createUserRequest.getUsers();
		if (null != idList) {
			for (int i = 0; i < userList.size(); i++) {
				userList.get(i).setId(idList.get(i));
				if (null != userList.get(i).getUserDetails()) {
/*					List<Address> addresses = userList.get(i).getUserDetails().getAddresses();
				if (null != addresses) {
						for (Address address : addresses) {
							address.setUserId(userList.get(i).getId());
							address.setTenantId(userList.get(i).getTenantId());
						}
					}*/
				}
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
		createUserRequest.setUsers(userList);
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

		if (null == searchCriteria.getIncludeDetails() || searchCriteria.getIncludeDetails() == false) {
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
		userProducer.producer(updateUserPasswordTopic, user);

		/*
		 * Long userId = requestInfo.getUserInfo().getId(); Otp otp =
		 * Otp.builder().tenantId(tenantId).identity(mobileNumber).otp(otpNumber
		 * ).auditDetails(auditDetails) .userId(userId).build();
		 * userProducer.producer(userSaveOtpTopic, otp);
		 */

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("tenantId", tenantId);
		hashMap.put("otp", otpNumber);
		hashMap.put("identity", mobileNumber);
		hashMap.put("auditDetails", auditDetails);
		userProducer.producer(userSmsNotification, hashMap);
	}

	public void createAndSendOtpForPasswordUpdate(RequestInfo requestInfo, String tenantId, String userName,
			String mobileNumber) {
		// TODO Auto-generated method stub
		String otpNumber = idGenRepository.generateUserOtpNumber(requestInfo, tenantId);

		UserSearchCriteria searchCriteria = UserSearchCriteria.builder().userName(userName).tenantId(tenantId)
				.includeDetails(true).build();
		User user = searchRepository.getAllUsers(requestInfo, searchCriteria).get(0);
		Long userId = requestInfo.getUserInfo().getId();
/*		AuditDetails auditDetails = AuditDetails.builder()
				.createdBy(userId).lastModifiedBy(userId).createdTime(new Date().getTime())
				.lastModifiedTime(new Date().getTime()).build();
		Otp otp = Otp.builder().tenantId(tenantId).identity(mobileNumber).otp(otpNumber).auditDetails(auditDetails)
				.userId(user.getId()).build(); 

		userProducer.producer(userSaveOtpTopic, otp); */

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("tenantId", tenantId);
		hashMap.put("otp", otpNumber);
		hashMap.put("identity", mobileNumber);
//		hashMap.put("auditDetails", auditDetails);
		userProducer.producer(userSmsNotification, hashMap);
	}
}
