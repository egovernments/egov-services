package org.egov.works.measurementbook.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object holds the basic data for Assets or Land assets for Estimate
 */
@ApiModel(description = "An Object holds the basic data for Assets or Land assets for Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class AssetsForEstimate   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("asset")
  private Asset asset = null;

  @JsonProperty("landAsset")
  private String landAsset = null;

  @JsonProperty("detailedEstimate")
  private String detailedEstimate = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public AssetsForEstimate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Assets For Estimate
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Assets For Estimate")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AssetsForEstimate tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Assets For Estimate
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Assets For Estimate")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AssetsForEstimate asset(Asset asset) {
    this.asset = asset;
    return this;
  }

   /**
   * Asset for which the Detailed Estimate is created. This field is required and to be shown if Nature of work is 'Repairs or Addition'.
   * @return asset
  **/
  @ApiModelProperty(value = "Asset for which the Detailed Estimate is created. This field is required and to be shown if Nature of work is 'Repairs or Addition'.")

  @Valid

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public AssetsForEstimate landAsset(String landAsset) {
    this.landAsset = landAsset;
    return this;
  }

   /**
   * Land Asset ID. This field needs to be shown if Nature of work is 'New' and landAssetRequired=true from Abstract Estimate.
   * @return landAsset
  **/
  @ApiModelProperty(value = "Land Asset ID. This field needs to be shown if Nature of work is 'New' and landAssetRequired=true from Abstract Estimate.")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+")
  public String getLandAsset() {
    return landAsset;
  }

  public void setLandAsset(String landAsset) {
    this.landAsset = landAsset;
  }

  public AssetsForEstimate detailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
    return this;
  }

   /**
   * Reference of the Detailed Estimate for Estimate and Assets linking
   * @return detailedEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference of the Detailed Estimate for Estimate and Assets linking")
  @NotNull


  public String getDetailedEstimate() {
    return detailedEstimate;
  }

  public void setDetailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
  }

  public AssetsForEstimate deleted(Boolean deleted) {
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

  public AssetsForEstimate auditDetails(AuditDetails auditDetails) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssetsForEstimate assetsForEstimate = (AssetsForEstimate) o;
    return Objects.equals(this.id, assetsForEstimate.id) &&
        Objects.equals(this.tenantId, assetsForEstimate.tenantId) &&
        Objects.equals(this.asset, assetsForEstimate.asset) &&
        Objects.equals(this.landAsset, assetsForEstimate.landAsset) &&
        Objects.equals(this.detailedEstimate, assetsForEstimate.detailedEstimate) &&
        Objects.equals(this.deleted, assetsForEstimate.deleted) &&
        Objects.equals(this.auditDetails, assetsForEstimate.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, asset, landAsset, detailedEstimate, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetsForEstimate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    landAsset: ").append(toIndentedString(landAsset)).append("\n");
    sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

