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
@NoArgsConstructor
@AllArgsConstructor
public class OpinionSearchCriteria {

	private String[] codes = null;

	@JsonProperty("opinionRequestDate")
	private Long opinionRequestDate = null;

	@JsonProperty("opinionsBy")
	private String opinionsBy = null;

	@JsonProperty("departmentName")
	private String departmentName = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("pageSize")
	private Integer pageSize;

	@JsonProperty("pageNumber")
	private Integer pageNumber;

	@JsonProperty("sort")
	private String sort;
	
	@JsonProperty("fromDate")
	private Long fromDate;
	
	@JsonProperty("toDate")
	private Long toDate;	
}
