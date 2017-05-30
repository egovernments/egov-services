package org.egov.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * PropertyDetail
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PropertyDetail {

	private Integer id;

	@Size(min=1, max=64)
	private String regdDocNo;

	private String regdDocDate;

	private String occupancyDate;

	@Size(min=1, max= 16)
	private String reason;

	@Size(min=1, max=4)
	private String status;

	private Boolean isVerified;

	private String verificationDate;

	private Boolean isExempted;

	@Size(min=1, max=32)
	private String exemptionReason;

	@Size(min=1, max= 16)
	private String propertyType;

	@Size(min=1, max= 16)
	private String category;

	@Size(min=1, max= 16)
	private String usage;

	@Size(min=1, max= 16)
	private String department;

	@Size(min=1, max= 16)
	private String apartment;

	private Double length;

	private Double breadth;

	private Double sitalArea;

	private Double totalBuiltupArea;

	private Double undividedShare;

	private Long noOfFloors;

	@Valid
	private List<Floor> floors;

	private Boolean isSuperStructure;

	@Size(min=1, max= 128)
	private String landOwner;

	@Size(min=1, max= 16)
	private String floorType;

	@Size(min=1, max= 16)
	private String woodType;

	@Size(min=1, max= 16)
	private String roofType;

	@Size(min=1, max= 16)
	private String wallType;

	private Boolean lift;

	private Boolean toilet;

	private Boolean waterTap;

	private Boolean electricity;

	private Boolean attachedBathRoom;

	private Boolean waterHarvesting;

	private Boolean cableConnection;
	
    @Valid
	private VacantLandProperty vacantLand;

	@NonNull
	@Size(min=4, max= 16)
	private String channel;

	@Size(min=1, max=32)
	private String applicationNo;

	private AuditDetails auditDetails;

	@NonNull
	@Size(min=4, max= 128)
	private String tenantId;

	private WorkflowDetails workFlowDetails;
	
	private List<Document> documents;
}
