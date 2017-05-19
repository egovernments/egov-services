package org.egov.models;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Property
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Property {

	private String id;

	@NonNull
	private String upicNo;

	private String oldUpicNo;

	private String vltUpicNo;

	@NonNull
	private String creationReason;

	private Address address;

	private List<User> owners;

	@NonNull

	private String assessmentDate;

	@NonNull

	private String occupancyDate;

	private String gisRefNo;

	private Double longitude;

	private Double latitude;

	private Long revenueZone;

	private Long revenueWard;

	private Long revenueBlock;

	private Long area;

	private Long locality;

	private Long street;

	private Long adminWard;

	private Boolean isAuthorised;

	private Boolean isUnderWorkflow;

	private PropertyBoundary propertyBoundary;

	private PropertyDetail propertydetails;

	@NonNull
	private String channel;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

	@NonNull
	private String tenantId;

}
