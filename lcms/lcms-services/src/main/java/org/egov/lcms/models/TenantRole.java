package org.egov.lcms.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Shubham 
 *	This object holds information about the tenant role
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantRole {
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("roles")
	private List<Role> roles = new ArrayList<Role>();

}
