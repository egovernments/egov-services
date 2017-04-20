package org.egov.eis.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Department {
	private Long id;
	private String name;
	private String code;
	private String tenantId = null;

	public Department(org.egov.eis.persistence.entity.Department entityDepartment) {
		id = entityDepartment.getId();
		name = entityDepartment.getName();
		code = entityDepartment.getCode();
		tenantId = entityDepartment.getTenantId();
	}
}
