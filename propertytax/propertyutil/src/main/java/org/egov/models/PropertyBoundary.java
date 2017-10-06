package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <h1>Property Boundary</h1>
 * 
 * @author S Anilkumar
 *
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyBoundary {

	private String id;

	private Double longitude;

	private Double latitude;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	private Integer revenueZone;

	private String revenueWard;

	private String revenueBlock;

	private String area;

	private String locality;

	private String street;

	private String adminWard;

	@Size(min = 1, max = 256)
	private String northBoundedBy;

	@Size(min = 1, max = 256)
	private String eastBoundedBy;

	@Size(min = 1, max = 256)
	private String westBoundedBy;

	@Size(min = 1, max = 256)
	private String southBoundedBy;

	private AuditDetails auditDetails;

	@Size(min = 4, max = 256)
	private String name;

}
