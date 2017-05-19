package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PropertyDetail
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PropertyDetail {

	private String id;

	private String regdDocNo;

	private LocalDate regdDocDate;

	private LocalDate occupancyDate;

	private String reason;

	private String status;

	private Boolean isVerified;

	private LocalDate verificationDate;

	private Boolean isExempted;

	private String exemptionReason;

	private String propertyType;

	private String category;

	private String usage;

	private String department;

	private String apartment;

	private Double length;

	private Double breadth;

	private Double sitalArea;

	private Double totalBuiltupArea;

	private Double undividedShare;

	private Long noOfFloors;

	private List<Floor> floors;

	private Boolean isSuperStructure;

	private String landOwner;

	private String floorType;

	private String woodType;

	private String roofType;

	private String wallType;

	private Boolean lift;

	private Boolean toilet;

	private Boolean waterTap;

	private Boolean electricity;

	private Boolean attachedBathRoom;

	private Boolean waterHarvesting;

	private Boolean cableConnection;

	private VacantLandProperty vacantLand;

	@NonNull
	private String channel;

	private String applicationNo;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private LocalDate lastModifiedDate;

	// @NonNull
	private String tenantId;

}
