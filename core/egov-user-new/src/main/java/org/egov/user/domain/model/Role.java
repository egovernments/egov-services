package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class Role {
	private static final String CITIZEN = "CITIZEN";

	private Long id;
	private String tenantId;
	private Long userId;
	private String code;
	private String name;
	private Long lastModifiedDate;

	public static Role getCitizenRole() {
		return Role.builder().code(CITIZEN).build();
	}
}
