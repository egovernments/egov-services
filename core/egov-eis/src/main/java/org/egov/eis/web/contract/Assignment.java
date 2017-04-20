package org.egov.eis.web.contract;

import javax.persistence.Column;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assignment {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("employee")
	private String employee = null;

	@JsonProperty("position")
	private String position = null;

	@JsonProperty("fund")
	private String fund = null;

	@JsonProperty("functionary")
	private String functionary = null;

	@JsonProperty("function")
	private String function = null;

	@JsonProperty("designation")
	private String designation = null;

	@JsonProperty("department")
	private String department = null;

	@JsonProperty("primary")
	private Boolean primary = null;

	@JsonProperty("fromDate")
	private LocalDate fromDate = null;

	@JsonProperty("toDate")
	private LocalDate toDate = null;

	@JsonProperty("grade")
	private String grade = null;

	@JsonProperty("govtOrderNumber")
	private String govtOrderNumber = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public Assignment(org.egov.eis.persistence.entity.Assignment entityAssignment) {
		id = entityAssignment.getId();
		employee = entityAssignment.getEmployee() != null ? entityAssignment.getEmployee().getName() : "";
		department = entityAssignment.getDepartment() != null ? entityAssignment.getDepartment().getName() : "";
		designation = entityAssignment.getDesignation() != null ? entityAssignment.getDesignation().getName() : "";
		fund = entityAssignment.getFund() != null ? entityAssignment.getFund().toString() : "";
		position = entityAssignment.getPosition() != null ? entityAssignment.getPosition().getId().toString() : "";
		tenantId = entityAssignment.getTenantId() != null ? entityAssignment.getTenantId().toString() : "";
	}

}
