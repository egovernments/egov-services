package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * This is the model class for the mutation master
 * 
 * @author Prasad
 * 
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("data")
public class MutationMaster {

	@JsonProperty("id")
	private Long id;

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@Size(min = 4, max = 128)
	@JsonProperty("name")
	private String name;

	@Size(min = 1, max = 64)
	@JsonProperty("code")
	private String code;

	@JsonProperty("data")
	private String data;

	@Size(min = 1, max = 256)
	@JsonProperty("nameLocal")
	private String nameLocal;

	@Size(min = 4, max = 512)
	@JsonProperty("description")
	private String description;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
