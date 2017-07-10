package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Prasad Model class of depreciation
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depreciation {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("code")
	@NotNull
	private String code;

	@JsonProperty("data")
	private String data;

	@JsonProperty("nameLocal")
	private String nameLocal;

	@JsonProperty("fromYear")
	private Integer fromYear;

	@JsonProperty("toYear")
	private Integer toyear;

	@JsonProperty("description")
	private String description;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
