package org.egov.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * VacantLandProperty
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VacantLandProperty {

	private Integer id;

	@Size(min=4, max=64)
	private String surveyNumber;
	
	@Size(min=4, max=64)
	private String pattaNumber;

	
	private Double marketValue;

	@Min(value=500)
	private Double capitalValue;
	
	@Size(min=4, max=64)
	@NotNull
	private String layoutApprovedAuth;
	
	@Size(min=4, max=64)
	private String layoutPermissionNo;

	@NotNull
	private String layoutPermissionDate;

	private Double resdPlotArea;

	private Double nonResdPlotArea;
	
	private AuditDetails auditDetails;

	@NonNull
	@Size(min=4, max=128)
	private String tenantId;

}
