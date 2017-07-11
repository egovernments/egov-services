package org.egov.models;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * This is the model class for the mutation master
 * 
 * @author Prasad
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutationMaster {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("data")
	private String data;

	@JsonProperty("nameLocal")
	private String nameLocal;

	@JsonProperty("description")
	private String description;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
