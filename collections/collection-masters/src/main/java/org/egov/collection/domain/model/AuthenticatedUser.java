package org.egov.collection.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticatedUser {
	private String mobileNumber;
	private String emailId;
	private String name;
	private Long id;
	private boolean anonymousUser;
	private List<Role> roles;
	private List<UserType> type;

	public static AuthenticatedUser createAnonymousUser() {
		final List<UserType> type = Collections.singletonList(UserType.SYSTEM);
		return AuthenticatedUser.builder().anonymousUser(true).type(type).id(BigDecimal.ZERO.longValue()).build();
	}

	public UserType getType() {
		return type.get(0);
	}

	public boolean isCitizen() {
		return getType() == UserType.CITIZEN;
	}
}
