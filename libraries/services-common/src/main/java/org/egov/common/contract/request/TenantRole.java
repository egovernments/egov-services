package org.egov.common.contract.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRole {
	
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("roles")
	private List<Role> roles;

}
