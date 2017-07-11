
package org.egov.asset.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	@NotNull
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("assetCategoryType")
	private AssetCategoryType assetCategoryType;

	@JsonProperty("parent")
	private Long parent;

	@JsonProperty("depreciationMethod")
	private DepreciationMethod depreciationMethod;

	@JsonProperty("isAssetAllow")
	private Boolean isAssetAllow;

	@JsonProperty("assetAccount")
	private Long assetAccount;

	@JsonProperty("accumulatedDepreciationAccount")
	private Long accumulatedDepreciationAccount;

	@JsonProperty("revaluationReserveAccount")
	private Long revaluationReserveAccount;

	@JsonProperty("depreciationExpenseAccount")
	private Long depreciationExpenseAccount;

	@JsonProperty("unitOfMeasurement")
	private Long unitOfMeasurement;

	@JsonProperty("version")
	private String version;

	@JsonProperty("depreciationRate")
	private Double depreciationRate;

	@JsonProperty("assetFieldsDefination")
	private List<AttributeDefination> assetFieldsDefination;
}
