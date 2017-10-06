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
 * @author Prasad Model class of depreciation
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "data" })
public class Depreciation {

	@JsonProperty("id")
	private Long id;

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@Size(min = 4, max = 64)
	@JsonProperty("code")
	@NotNull
	private String code;

	@JsonProperty("data")
	private String data;

	@JsonProperty("nameLocal")
	private String nameLocal;

	@JsonProperty("fromYear")
	private Integer fromYear;

	@JsonProperty("year")
	private Integer year;

	@JsonProperty("toYear")
	private Integer toyear;

	@Size(min = 8, max = 512)
	@JsonProperty("description")
	private String description;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
