package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 *Model class for Demolition
 * 
 * @author vishal
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DemolitionReason {
	
	@JsonProperty("id")
	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;

	@NotNull
	@JsonProperty("name")
	@Size(min = 4, max = 128)
	private String name;

	@NotNull
	@JsonProperty("code")
	@Size(min = 1, max = 64)
	private String code;
	
	@JsonProperty("description")
	@Size(min = 4, max = 512)
	private String description;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;


}
