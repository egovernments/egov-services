package org.egov.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AuthenticatedUser {
	private String mobileNumber;
	private String emailId;
	private String name;
	private Long id;
	private boolean anonymousUser;
	private List<Role> roles;
	private List<UserType> type;


}
