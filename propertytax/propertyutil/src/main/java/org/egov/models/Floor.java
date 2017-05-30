package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Floor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Floor {
	
	private Integer id;

	@NotNull
	@Size(min=1, max= 16)
	private String floorNo;
	
	@Size(min=1, max= 8)
	private String unitNo;
	
	@Size(min=1, max= 16)
	private String type;

	private Double length;

	private Double width;

	@NotNull
	private Double builtupArea;

	private Double assessableArea;

	private Double bpaBuiltupArea;

	@Size(min=1, max= 16)
	private String category;

	@NotNull
	@Size(min=1, max= 16)
	private String usage;

	@NotNull
	@Size(min=1, max= 16)
	private String occupancy;

	@NotNull
	@Size(min=1, max= 16)
	private String structure;

	@Size(min=1, max= 16)
	private String depreciation;
	
	@Size(min=1, max= 128)
	private String occupierName;
	
	@Size(min=1, max= 128)
	private String firmName;

	private Double rentCollected;

	@Size(min=1, max=32)
	private String exemptionReason;

	private Boolean isStructured;

	private String occupancyDate;

	private String constCompletionDate;

	@Size(min=1, max= 16)
	private String bpaNo;

	private String bpaDate;

	private Double manualArv;

	private Double arv;

	@Size(min=1, max=64)
	private String electricMeterNo;
	
	@Size(min=1, max= 64)
	private String waterMeterNo;

	private AuditDetails auditDetails;
	
    @NotNull
    @Size(min=4, max= 128)
	private String tenantId;

}
