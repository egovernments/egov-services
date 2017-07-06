
package org.egov.asset.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;

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

	@NotNull
	private String tenantId;

	private Long id;

	@NotNull
	private String name;

	private String code;

	private AssetCategoryType assetCategoryType;

	private Long parent;

	private DepreciationMethod depreciationMethod;

	private Boolean isAssetAllow;

	private Long assetAccount;

	private Long accumulatedDepreciationAccount;

	private Long revaluationReserveAccount;

	private Long depreciationExpenseAccount;

	private Long unitOfMeasurement;

	private String version;

	private Double depreciationRate;

	private List<AttributeDefinition> assetFieldsDefination;
}
