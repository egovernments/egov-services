package org.egov.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.model.enums.AssetCategoryType;
import org.egov.model.enums.DepreciationMethod;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Categories defined under asset category type are shown in the drop down.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AssetCategory {


	private String tenantId;

	@NotNull
	private Long id;

	
	private String name;

	private String code;
    
	private AssetCategoryType assetCategoryType;

	private Long parent;

	private DepreciationMethod depreciationMethod;

	private Boolean isAssetAllow;

	private String assetAccount;

	private String accumulatedDepreciationAccount;

	private String revaluationReserveAccount;

	private String depreciationExpenseAccount;

	private String unitOfMeasurement;

	private String version;
	
	private Boolean isDepreciationApplicable;

	private Double depreciationRate;

	private List<AttributeDefinition> assetFieldsDefination;
}
