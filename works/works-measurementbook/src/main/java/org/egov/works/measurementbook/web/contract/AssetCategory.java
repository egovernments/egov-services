package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Assets are calssified under various asset categories based on their properties. This master data is defined under MDMS.
 */
@ApiModel(description = "Assets are calssified under various asset categories based on their properties. This master data is defined under MDMS.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class AssetCategory   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  /**
   * Asset Category Type information.
   */
  public enum AssetCategoryTypeEnum {
    LAND("LAND"),
    
    MOVABLE("MOVABLE"),
    
    IMMOVABLE("IMMOVABLE");

    private String value;

    AssetCategoryTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AssetCategoryTypeEnum fromValue(String text) {
      for (AssetCategoryTypeEnum b : AssetCategoryTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("assetCategoryType")
  private AssetCategoryTypeEnum assetCategoryType = null;

  @JsonProperty("parent")
  private Long parent = null;

  @JsonProperty("isDepreciationApplicable")
  private Boolean isDepreciationApplicable = null;

  /**
   * Depreciation Method of asset for this asset category.
   */
  public enum DepreciationMethodEnum {
    METHOD("Straight Line Method");

    private String value;

    DepreciationMethodEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DepreciationMethodEnum fromValue(String text) {
      for (DepreciationMethodEnum b : DepreciationMethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("depreciationMethod")
  private DepreciationMethodEnum depreciationMethod = null;

  @JsonProperty("isAssetAllow")
  private Boolean isAssetAllow = null;

  @JsonProperty("assetAccount")
  private String assetAccount = null;

  @JsonProperty("accumulatedDepreciationAccount")
  private String accumulatedDepreciationAccount = null;

  @JsonProperty("revaluationReserveAccount")
  private String revaluationReserveAccount = null;

  @JsonProperty("depreciationExpenseAccount")
  private String depreciationExpenseAccount = null;

  @JsonProperty("unitOfMeasurement")
  private String unitOfMeasurement = null;

  @JsonProperty("version")
  private String version = null;

  @JsonProperty("depreciationRate")
  private Integer depreciationRate = null;

  @JsonProperty("assetFieldsDefination")
  private List<AttributeDefinition> assetFieldsDefination = null;

  public AssetCategory tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique id for ulb.
   * @return tenantId
  **/
  @ApiModelProperty(value = "Unique id for ulb.")


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AssetCategory id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier of Category
   * @return id
  **/
  @ApiModelProperty(value = "Unique identifier of Category")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AssetCategory name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the Asset Category.
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Name of the Asset Category.")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AssetCategory code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique code for the asset category.  This will be auto generated or specified by the user based on implementation specific requirement.
   * @return code
  **/
  @ApiModelProperty(value = "Unique code for the asset category.  This will be auto generated or specified by the user based on implementation specific requirement.")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public AssetCategory assetCategoryType(AssetCategoryTypeEnum assetCategoryType) {
    this.assetCategoryType = assetCategoryType;
    return this;
  }

   /**
   * Asset Category Type information.
   * @return assetCategoryType
  **/
  @ApiModelProperty(required = true, value = "Asset Category Type information.")
  @NotNull


  public AssetCategoryTypeEnum getAssetCategoryType() {
    return assetCategoryType;
  }

  public void setAssetCategoryType(AssetCategoryTypeEnum assetCategoryType) {
    this.assetCategoryType = assetCategoryType;
  }

  public AssetCategory parent(Long parent) {
    this.parent = parent;
    return this;
  }

   /**
   * Options will be the already created list of asset category in the master.
   * @return parent
  **/
  @ApiModelProperty(value = "Options will be the already created list of asset category in the master.")


  public Long getParent() {
    return parent;
  }

  public void setParent(Long parent) {
    this.parent = parent;
  }

  public AssetCategory isDepreciationApplicable(Boolean isDepreciationApplicable) {
    this.isDepreciationApplicable = isDepreciationApplicable;
    return this;
  }

   /**
   * if value is true then we can add depreciaiton percentage will need to be captured.
   * @return isDepreciationApplicable
  **/
  @ApiModelProperty(value = "if value is true then we can add depreciaiton percentage will need to be captured.")


  public Boolean getIsDepreciationApplicable() {
    return isDepreciationApplicable;
  }

  public void setIsDepreciationApplicable(Boolean isDepreciationApplicable) {
    this.isDepreciationApplicable = isDepreciationApplicable;
  }

  public AssetCategory depreciationMethod(DepreciationMethodEnum depreciationMethod) {
    this.depreciationMethod = depreciationMethod;
    return this;
  }

   /**
   * Depreciation Method of asset for this asset category.
   * @return depreciationMethod
  **/
  @ApiModelProperty(value = "Depreciation Method of asset for this asset category.")


  public DepreciationMethodEnum getDepreciationMethod() {
    return depreciationMethod;
  }

  public void setDepreciationMethod(DepreciationMethodEnum depreciationMethod) {
    this.depreciationMethod = depreciationMethod;
  }

  public AssetCategory isAssetAllow(Boolean isAssetAllow) {
    this.isAssetAllow = isAssetAllow;
    return this;
  }

   /**
   * if value is true then we can add asset directly in this particular category.
   * @return isAssetAllow
  **/
  @ApiModelProperty(value = "if value is true then we can add asset directly in this particular category.")


  public Boolean getIsAssetAllow() {
    return isAssetAllow;
  }

  public void setIsAssetAllow(Boolean isAssetAllow) {
    this.isAssetAllow = isAssetAllow;
  }

  public AssetCategory assetAccount(String assetAccount) {
    this.assetAccount = assetAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Fixed Asset\".
   * @return assetAccount
  **/
  @ApiModelProperty(value = "Options are from the  chart of account master for the account code purpose \"Fixed Asset\".")


  public String getAssetAccount() {
    return assetAccount;
  }

  public void setAssetAccount(String assetAccount) {
    this.assetAccount = assetAccount;
  }

  public AssetCategory accumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
    this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Accumulated Depreciation\".
   * @return accumulatedDepreciationAccount
  **/
  @ApiModelProperty(value = "Options are from the  chart of account master for the account code purpose \"Accumulated Depreciation\".")


  public String getAccumulatedDepreciationAccount() {
    return accumulatedDepreciationAccount;
  }

  public void setAccumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
    this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
  }

  public AssetCategory revaluationReserveAccount(String revaluationReserveAccount) {
    this.revaluationReserveAccount = revaluationReserveAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Revaluation Reserve Account\".
   * @return revaluationReserveAccount
  **/
  @ApiModelProperty(value = "Options are from the  chart of account master for the account code purpose \"Revaluation Reserve Account\".")


  public String getRevaluationReserveAccount() {
    return revaluationReserveAccount;
  }

  public void setRevaluationReserveAccount(String revaluationReserveAccount) {
    this.revaluationReserveAccount = revaluationReserveAccount;
  }

  public AssetCategory depreciationExpenseAccount(String depreciationExpenseAccount) {
    this.depreciationExpenseAccount = depreciationExpenseAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Depreciation Expense Account\".
   * @return depreciationExpenseAccount
  **/
  @ApiModelProperty(value = "Options are from the  chart of account master for the account code purpose \"Depreciation Expense Account\".")


  public String getDepreciationExpenseAccount() {
    return depreciationExpenseAccount;
  }

  public void setDepreciationExpenseAccount(String depreciationExpenseAccount) {
    this.depreciationExpenseAccount = depreciationExpenseAccount;
  }

  public AssetCategory unitOfMeasurement(String unitOfMeasurement) {
    this.unitOfMeasurement = unitOfMeasurement;
    return this;
  }

   /**
   * The unique cide of the unit of measurement that has to be used when referring any assets coming under this category. This will come from the UOM master in MDMS.
   * @return unitOfMeasurement
  **/
  @ApiModelProperty(required = true, value = "The unique cide of the unit of measurement that has to be used when referring any assets coming under this category. This will come from the UOM master in MDMS.")
  @NotNull


  public String getUnitOfMeasurement() {
    return unitOfMeasurement;
  }

  public void setUnitOfMeasurement(String unitOfMeasurement) {
    this.unitOfMeasurement = unitOfMeasurement;
  }

  public AssetCategory version(String version) {
    this.version = version;
    return this;
  }

   /**
   * version of the asset category.
   * @return version
  **/
  @ApiModelProperty(value = "version of the asset category.")


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public AssetCategory depreciationRate(Integer depreciationRate) {
    this.depreciationRate = depreciationRate;
    return this;
  }

   /**
   * Depreciation rate for a asset category. That will be always defined in percentage. This value is required for all categories other than  the Asset category type \"Land\".
   * @return depreciationRate
  **/
  @ApiModelProperty(value = "Depreciation rate for a asset category. That will be always defined in percentage. This value is required for all categories other than  the Asset category type \"Land\".")


  public Integer getDepreciationRate() {
    return depreciationRate;
  }

  public void setDepreciationRate(Integer depreciationRate) {
    this.depreciationRate = depreciationRate;
  }

  public AssetCategory assetFieldsDefination(List<AttributeDefinition> assetFieldsDefination) {
    this.assetFieldsDefination = assetFieldsDefination;
    return this;
  }

  public AssetCategory addAssetFieldsDefinationItem(AttributeDefinition assetFieldsDefinationItem) {
    if (this.assetFieldsDefination == null) {
      this.assetFieldsDefination = new ArrayList<AttributeDefinition>();
    }
    this.assetFieldsDefination.add(assetFieldsDefinationItem);
    return this;
  }

   /**
   * Custom Fields.
   * @return assetFieldsDefination
  **/
  @ApiModelProperty(value = "Custom Fields.")

  @Valid

  public List<AttributeDefinition> getAssetFieldsDefination() {
    return assetFieldsDefination;
  }

  public void setAssetFieldsDefination(List<AttributeDefinition> assetFieldsDefination) {
    this.assetFieldsDefination = assetFieldsDefination;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssetCategory assetCategory = (AssetCategory) o;
    return Objects.equals(this.tenantId, assetCategory.tenantId) &&
        Objects.equals(this.id, assetCategory.id) &&
        Objects.equals(this.name, assetCategory.name) &&
        Objects.equals(this.code, assetCategory.code) &&
        Objects.equals(this.assetCategoryType, assetCategory.assetCategoryType) &&
        Objects.equals(this.parent, assetCategory.parent) &&
        Objects.equals(this.isDepreciationApplicable, assetCategory.isDepreciationApplicable) &&
        Objects.equals(this.depreciationMethod, assetCategory.depreciationMethod) &&
        Objects.equals(this.isAssetAllow, assetCategory.isAssetAllow) &&
        Objects.equals(this.assetAccount, assetCategory.assetAccount) &&
        Objects.equals(this.accumulatedDepreciationAccount, assetCategory.accumulatedDepreciationAccount) &&
        Objects.equals(this.revaluationReserveAccount, assetCategory.revaluationReserveAccount) &&
        Objects.equals(this.depreciationExpenseAccount, assetCategory.depreciationExpenseAccount) &&
        Objects.equals(this.unitOfMeasurement, assetCategory.unitOfMeasurement) &&
        Objects.equals(this.version, assetCategory.version) &&
        Objects.equals(this.depreciationRate, assetCategory.depreciationRate) &&
        Objects.equals(this.assetFieldsDefination, assetCategory.assetFieldsDefination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, id, name, code, assetCategoryType, parent, isDepreciationApplicable, depreciationMethod, isAssetAllow, assetAccount, accumulatedDepreciationAccount, revaluationReserveAccount, depreciationExpenseAccount, unitOfMeasurement, version, depreciationRate, assetFieldsDefination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetCategory {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    assetCategoryType: ").append(toIndentedString(assetCategoryType)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    isDepreciationApplicable: ").append(toIndentedString(isDepreciationApplicable)).append("\n");
    sb.append("    depreciationMethod: ").append(toIndentedString(depreciationMethod)).append("\n");
    sb.append("    isAssetAllow: ").append(toIndentedString(isAssetAllow)).append("\n");
    sb.append("    assetAccount: ").append(toIndentedString(assetAccount)).append("\n");
    sb.append("    accumulatedDepreciationAccount: ").append(toIndentedString(accumulatedDepreciationAccount)).append("\n");
    sb.append("    revaluationReserveAccount: ").append(toIndentedString(revaluationReserveAccount)).append("\n");
    sb.append("    depreciationExpenseAccount: ").append(toIndentedString(depreciationExpenseAccount)).append("\n");
    sb.append("    unitOfMeasurement: ").append(toIndentedString(unitOfMeasurement)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    depreciationRate: ").append(toIndentedString(depreciationRate)).append("\n");
    sb.append("    assetFieldsDefination: ").append(toIndentedString(assetFieldsDefination)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

