package org.egov.workflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

	@Value("${egov.services.eis.host}")
	private String eisServiceHostname;

	@Value("${egov.services.eis.position_by_id}")
	private String getPositionsByIdContext;

	@Value("${egov.services.eis.position_by_employee_code}")
	private String getPositionByEmployeeCodeContext;

	@Value("${egov.services.eis.positionhierarchys}")
	private String getPositionHierarchysContext;

	@Value("${egov.services.eis.employee_by_role}")
	private String getEmployeesByRoleContext;

	@Value("${egov.services.eis.employee_by_position}")
	private String getEmployeeByPositionContext;

	@Value("${egov.services.eis.employee_by_userid}")
	private String getEmployeeByUserIdContext;

	public String getPositionsByIdUrl() {
		return this.eisServiceHostname + this.getPositionsByIdContext;
	}

	public String getPositionByEmployeeCodeUrl() {
		return this.eisServiceHostname + this.getPositionByEmployeeCodeContext;
	}

	public String getPositionHierarchyUrl() {
		return this.eisServiceHostname + this.getPositionHierarchysContext;
	}

	public String getEmployeeByRoleNameUrl() {
		return this.eisServiceHostname + this.getEmployeesByRoleContext;
	}

	public String getEmployeeByPositionUrl() {
		return this.eisServiceHostname + this.getEmployeeByPositionContext;
	}

	public String getEmployeeByUserIdUrl() {
		return this.eisServiceHostname + this.getEmployeeByUserIdContext;
	}
}
