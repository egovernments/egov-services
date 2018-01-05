package org.egov.works.measurementbook.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Asset information for the Contractor Bill
 */
@ApiModel(description = "Asset information for the Contractor Bill")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class AssetForBill   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("asset")
  private Asset asset = null;

  @JsonProperty("chartOfAccounts")
  private ChartOfAccount chartOfAccounts = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("contractorBill")
  private String contractorBill = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  public AssetForBill id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Asset for Contractor Bill
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Asset for Contractor Bill")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AssetForBill tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Asset for Contractor Bill
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Asset for Contractor Bill")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AssetForBill asset(Asset asset) {
    this.asset = asset;
    return this;
  }

   /**
   * Asset Reference from Asset module
   * @return asset
  **/
  @ApiModelProperty(required = true, value = "Asset Reference from Asset module")
  @NotNull

  @Valid

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public AssetForBill chartOfAccounts(ChartOfAccount chartOfAccounts) {
    this.chartOfAccounts = chartOfAccounts;
    return this;
  }

   /**
   * Chart of Accounts for the Assets for Bill
   * @return chartOfAccounts
  **/
  @ApiModelProperty(value = "Chart of Accounts for the Assets for Bill")

  @Valid

  public ChartOfAccount getChartOfAccounts() {
    return chartOfAccounts;
  }

  public void setChartOfAccounts(ChartOfAccount chartOfAccounts) {
    this.chartOfAccounts = chartOfAccounts;
  }

  public AssetForBill description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description for the Asset for Bill
   * @return description
  **/
  @ApiModelProperty(value = "Description for the Asset for Bill")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AssetForBill amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Asset wise Amount for the Bill
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "Asset wise Amount for the Bill")
  @NotNull

  @Valid

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public AssetForBill contractorBill(String contractorBill) {
    this.contractorBill = contractorBill;
    return this;
  }

   /**
   * Bill reference in the Asset
   * @return contractorBill
  **/
  @ApiModelProperty(required = true, value = "Bill reference in the Asset")
  @NotNull


  public String getContractorBill() {
    return contractorBill;
  }

  public void setContractorBill(String contractorBill) {
    this.contractorBill = contractorBill;
  }

  public AssetForBill auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }

  public AssetForBill deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Boolean value to identify whether the object is deleted or not from UI.
   * @return deleted
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssetForBill assetForBill = (AssetForBill) o;
    return Objects.equals(this.id, assetForBill.id) &&
        Objects.equals(this.tenantId, assetForBill.tenantId) &&
        Objects.equals(this.asset, assetForBill.asset) &&
        Objects.equals(this.chartOfAccounts, assetForBill.chartOfAccounts) &&
        Objects.equals(this.description, assetForBill.description) &&
        Objects.equals(this.amount, assetForBill.amount) &&
        Objects.equals(this.contractorBill, assetForBill.contractorBill) &&
        Objects.equals(this.auditDetails, assetForBill.auditDetails) &&
        Objects.equals(this.deleted, assetForBill.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, asset, chartOfAccounts, description, amount, contractorBill, auditDetails, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetForBill {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    chartOfAccounts: ").append(toIndentedString(chartOfAccounts)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    contractorBill: ").append(toIndentedString(contractorBill)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

