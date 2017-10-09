package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Penalty {

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("period")
	private String period = null;

	@JsonProperty("amount")
	private Double amount = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
