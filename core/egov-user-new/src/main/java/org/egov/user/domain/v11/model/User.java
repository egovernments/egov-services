package org.egov.user.domain.v11.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Long id;

	@NotNull
	private String name;
	@NotNull
	private String userName;
	private String password;
	private String salutation;
	@NotNull
	private String gender;
	@NotNull
	private String mobileNumber;
	private String emailId;
	private String aadhaarNumber;
	@NotNull
	private Boolean active;
	private Long pwdExpiryDate;
	@NotNull
	private String locale;
	@NotNull
	private String type;
	private Boolean accountLocked;
	@NotNull
	private String tenantId;
	private List<Role> roles;
	private UserDetails userDetails;
	private AuditDetails auditDetails;


	public void setRoleToCitizen() {
		roles = Collections.singletonList(Role.getCitizenRole());
	}
	public void setDefaultPasswordExpiry(int expiryInDays) {
		if (pwdExpiryDate == null) {
			pwdExpiryDate = DateUtils.addDays(new Date(), expiryInDays).getTime();
		}
	}
}
