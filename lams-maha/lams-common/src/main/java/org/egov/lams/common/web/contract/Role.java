package org.egov.lams.common.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * minimal representation of the Roles in the system to be carried along in
 * UserInfo with RequestInfo meta data. Actual authorization service to extend
 * this to have more role related attributes Author : Narendra
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	@NotNull
	@Size(min = 2, max = 100)
	private String name = null;

	@JsonProperty("code")
	@Size(min = 2, max = 50)
	private String code = null;

	@JsonProperty("description")
	@Size(max = 256)
	private String description = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

}
