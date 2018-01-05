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
 * An Object that holds the basic data of Assets or Land assets for LOA
 */
@ApiModel(description = "An Object that holds the basic data of Assets or Land assets for LOA")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class AssetsForLOA   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("asset")
  private Asset asset = null;

  @JsonProperty("landAsset")
  private String landAsset = null;

  @JsonProperty("letterOfAcceptanceEstimate")
  private String letterOfAcceptanceEstimate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  public AssetsForLOA id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Assets for LOA
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Assets for LOA")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AssetsForLOA tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Assets for LOA
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Assets for LOA")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AssetsForLOA asset(Asset asset) {
    this.asset = asset;
    return this;
  }

   /**
   * Asset for which this LOA is created. This is required if Nature of work is 'Repairs or Addition' from Abstract/Detailed Estimate.
   * @return asset
  **/
  @ApiModelProperty(required = true, value = "Asset for which this LOA is created. This is required if Nature of work is 'Repairs or Addition' from Abstract/Detailed Estimate.")
  @NotNull

  @Valid

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public AssetsForLOA landAsset(String landAsset) {
    this.landAsset = landAsset;
    return this;
  }

   /**
   * Land Asset ID. This field needs to be shown if Nature of work is 'New' and landAssetRequired=true from Abstract/Detailed Estimate.
   * @return landAsset
  **/
  @ApiModelProperty(value = "Land Asset ID. This field needs to be shown if Nature of work is 'New' and landAssetRequired=true from Abstract/Detailed Estimate.")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+")
  public String getLandAsset() {
    return landAsset;
  }

  public void setLandAsset(String landAsset) {
    this.landAsset = landAsset;
  }

  public AssetsForLOA letterOfAcceptanceEstimate(String letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    return this;
  }

   /**
   * LOA Estimate reference, primary key is reference here.
   * @return letterOfAcceptanceEstimate
  **/
  @ApiModelProperty(required = true, value = "LOA Estimate reference, primary key is reference here.")
  @NotNull


  public String getLetterOfAcceptanceEstimate() {
    return letterOfAcceptanceEstimate;
  }

  public void setLetterOfAcceptanceEstimate(String letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
  }

  public AssetsForLOA auditDetails(AuditDetails auditDetails) {
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

  public AssetsForLOA deleted(Boolean deleted) {
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
    AssetsForLOA assetsForLOA = (AssetsForLOA) o;
    return Objects.equals(this.id, assetsForLOA.id) &&
        Objects.equals(this.tenantId, assetsForLOA.tenantId) &&
        Objects.equals(this.asset, assetsForLOA.asset) &&
        Objects.equals(this.landAsset, assetsForLOA.landAsset) &&
        Objects.equals(this.letterOfAcceptanceEstimate, assetsForLOA.letterOfAcceptanceEstimate) &&
        Objects.equals(this.auditDetails, assetsForLOA.auditDetails) &&
        Objects.equals(this.deleted, assetsForLOA.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, asset, landAsset, letterOfAcceptanceEstimate, auditDetails, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetsForLOA {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    landAsset: ").append(toIndentedString(landAsset)).append("\n");
    sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
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

