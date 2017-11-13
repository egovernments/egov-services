package org.egov.lcms.notification.model;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attender {
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("name")
	private String name = null;

	@Size(min = 10, max = 10)
	@JsonProperty("mobileNumber")
	private String mobileNumber = null;

	@JsonProperty("gender")
	private String gender = null;

	@JsonProperty("address")
	private Address address = null;

	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
