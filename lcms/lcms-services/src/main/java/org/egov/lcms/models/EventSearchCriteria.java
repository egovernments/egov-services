package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSearchCriteria {

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("departmentConcernPerson")
	private String departmentConcernPerson;

	@JsonProperty("entity")
	private String entity;

	@JsonProperty("moduleName")
	private String moduleName;

	@JsonProperty("sort")
	private String sort;
}
