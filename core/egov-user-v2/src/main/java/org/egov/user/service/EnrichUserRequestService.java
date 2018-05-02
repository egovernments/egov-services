package org.egov.user.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.Type;
import org.egov.user.contract.Address;
import org.egov.user.contract.User;
import org.egov.user.utils.AuditDetailsPopulator;
import org.springframework.stereotype.Component;

@Component
public class EnrichUserRequestService {

	// TODO: IF we can call this method from validator
	public void enrichUserRequest(List<User> users, RequestInfo requestInfo) {
		for (User user : users) {
			setUserUUId(user);
			setUserRole(user);

			if (requestInfo.getUserInfo() != null)
				user.setAuditDetails(AuditDetailsPopulator.getAuditDetail(requestInfo.getUserInfo().getId(), false));
			else
				user.setAuditDetails(AuditDetailsPopulator.getAuditDetail(user.getId(), false));
		}
	}

	private void setUserUUId(User user) {
		user.setId(UUID.randomUUID().toString());
		setUserDetailUUId(user);
	}

	// TODO: Null check for userDetails
	private void setUserDetailUUId(User user) {
		user.getUserDetails().setUuid(UUID.randomUUID().toString());
		user.getUserDetails().getAddresses().forEach(address -> address.setUuid(UUID.randomUUID().toString()));
	}
	
	private void setUserRole(User user) {

		if (user.getType().toString().equals(Type.CITIZEN.toString())) {
			user.setPrimaryrole(Collections.singletonList(Role.builder().code(Type.CITIZEN.toString()).build()));
			// TODO: ElseIf will required RequestInfo validation userinfo
			// validation
		} else if (user.getType().toString().equals(Type.EMPLOYEE.toString())) {
			boolean hasRoleEmployee = user.getPrimaryrole().stream()
					.anyMatch(role -> role.getCode().equals(Type.EMPLOYEE.toString()));

			if (!hasRoleEmployee)
				user.getPrimaryrole().add(Role.builder().code(Type.EMPLOYEE.toString()).build());
		}
	}
	
	private void setActiveFlag(User user) {
		user.setActive(false);
	}

	// TODO:
	private void setPasswordExpiryDate() {

	}

}
