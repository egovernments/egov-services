package org.egov.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.user.model.Address;
import org.egov.user.model.TenantRole;
import org.egov.user.model.User;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.model.enums.DbAction;
import org.egov.user.repository.NewUserRepository;
import org.egov.user.utils.UserConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewUserService {

	@Autowired
	private NewUserRepository userRepository;

	@Autowired
	private UserIdPopuater userIdPopuater;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserConfigurationUtil userConfigurationUtil;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public UserRes search(UserSearchCriteria userSearchCriteria) {
		return getResponse(userRepository.search(userSearchCriteria));
	}

	public UserRes createAsync(UserReq userReq) {
		try {
			userIdPopuater.populateId(userReq);
			encodePassword(userReq.getUsers());
			kafkaTemplate.send(userConfigurationUtil.getCreateUserTopic(), userReq);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return getResponse(userReq.getUsers());
	}

	public UserRes updateAsync(UserReq userReq) {
		List<User> users = userReq.getUsers();

		log.info("updateAsync initial userReq :" + userReq);
		Set<Long> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toSet());
		UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().id(userIds)
				.tenantId(users.get(0).getTenantId()).build();
		log.info("updateAsync userSearchCriteria :" + userSearchCriteria);
		List<User> dbUsers = userRepository.search(userSearchCriteria);
		log.info("updateAsync dbUsers :" + dbUsers);
		Map<Long, User> dbUsermap = dbUsers.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		List<Address> newAdresses = new ArrayList<>();
		List<TenantRole> newTenantRole = new ArrayList<>();
		if (users.size() == dbUsers.size()) {
			for (User user : users) {
				User dbUser = dbUsermap.get(user.getId());

				List<TenantRole> tenantRoles = user.getAdditionalroles();
				List<TenantRole> dbTenantRoles = dbUser.getAdditionalroles();
				List<Address> addresses = user.getUserDetails().getAddresses();
				List<Address> dbAddresses = dbUser.getUserDetails().getAddresses();

				Map<Long, Address> dbAddressmap = dbAddresses.stream()
						.collect(Collectors.toMap(Address::getId, Function.identity()));
				Map<Long, TenantRole> dbTenantRolemap = dbTenantRoles.stream()
						.collect(Collectors.toMap(TenantRole::getId, Function.identity()));

				for (Address address : addresses) {
					Address dbAddress = dbAddressmap.get(address.getId());
					if (dbAddress != null) {
						address.setDbAction(DbAction.UPDATE);
					} else {
						address.setDbAction(DbAction.INSERT);
						newAdresses.add(address);
					}
				}

				for (TenantRole tenantRole : tenantRoles) {
					System.out.println("updateAsync dbTenantRolemap: " + dbTenantRolemap);
					System.out.println("updateAsync tenantRole.getId(): " + tenantRole.getId());
					TenantRole dbTenantRole = dbTenantRolemap.get(tenantRole.getId());
					log.info("updateAsync dbTenantRole: " + dbTenantRole);
					if (dbTenantRole != null) {
						log.info("updateAsync dbTenantRole1: " + dbTenantRole);
						tenantRole.setDbAction(DbAction.UPDATE);
					} else {
						log.info("updateAsync dbTenantRole2: " + dbTenantRole);
						tenantRole.setDbAction(DbAction.INSERT);
						newTenantRole.add(tenantRole);
					}
				}

			}

		} else {
			throw new RuntimeException("Invalid user update list");
		}
		log.info("updateAsync before id populator userReq :" + userReq);
		userIdPopuater.populateAddressAndUserTenantId(newAdresses, newTenantRole);
		log.info("updateAsync after id populator userReq :" + userReq);

		try {
			encodePassword(users);
			kafkaTemplate.send(userConfigurationUtil.getUpdateUserTopic(), userReq);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return getResponse(userReq.getUsers());
	}

	private void encodePassword(List<User> users) {
		for (User user : users) {
			String encPass = passwordEncoder.encode(user.getPassword());
			user.setPassword(encPass);
		}
	}

	public UserRes getResponse(List<User> users) {
		UserRes userRes = new UserRes();
		userRes.setUsers(users);
		return userRes;
	}

}
