package org.egov.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * VacantLandDetail Author : Narendra
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VacantLandDetail {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("surveyNumber")
	@Size(min = 4, max = 64)
	private String surveyNumber = null;

	@JsonProperty("pattaNumber")
	@Size(min = 4, max = 64)
	private String pattaNumber = null;

	@JsonProperty("marketValue")
	private Double marketValue = null;

	@JsonProperty("capitalValue")
	private Double capitalValue = null;

	@JsonProperty("layoutApprovedAuth")
	@Size(min = 4, max = 64)
	private String layoutApprovedAuth = null;

	@JsonProperty("layoutPermissionNo")
	@Size(min = 4, max = 64)
	private String layoutPermissionNo = null;

	@JsonProperty("layoutPermissionDate")
	private String layoutPermissionDate = null;

	@JsonProperty("resdPlotArea")
	private Double resdPlotArea = null;

	@JsonProperty("nonResdPlotArea")
	private Double nonResdPlotArea = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
