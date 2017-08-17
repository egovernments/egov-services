package org.egov.access.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleAction {

	private String roleCode;

	private long actionId;

	private String tenantId;

}
