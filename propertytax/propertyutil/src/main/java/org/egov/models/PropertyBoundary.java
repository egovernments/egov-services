package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Boundary
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyBoundary {

	private String id;

	private String longitude;

	private String latitude;

	private String materializedPath;

	@NonNull
	private String tenantId;

	private String revenueZone;

	private String revenueWard;

	private String revenueBlock;

	private String area;

	private String locality;

	private String street;

	private String adminWard;

	private String northBoundedBy;

	private String eastBoundedBy;

	private String westBoundedBy;

	private String southBoundedBy;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

}
