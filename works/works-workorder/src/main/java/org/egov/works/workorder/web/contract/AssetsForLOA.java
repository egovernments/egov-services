package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds the basic data of Assets for LOA
 */
@ApiModel(description = "An Object that holds the basic data of Assets for LOA")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-11T12:10:45.989Z")

public class AssetsForLOA   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("asset")
  private String asset = null;

  @JsonProperty("letterOfAcceptanceEstimate")
  private String letterOfAcceptanceEstimate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

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

  public AssetsForLOA asset(String asset) {
    this.asset = asset;
    return this;
  }

   /**
   * Asset for which this LOA is created, code from 'Asset' is ref. here.
   * @return asset
  **/
  @ApiModelProperty(required = true, value = "Asset for which this LOA is created, code from 'Asset' is ref. here.")
  @NotNull


  public String getAsset() {
    return asset;
  }

  public void setAsset(String asset) {
    this.asset = asset;
  }

  public AssetsForLOA letterOfAcceptanceEstimate(String letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    return this;
  }

   /**
   * LOA Estimate reference, primary key is ref. here.
   * @return letterOfAcceptanceEstimate
  **/
  @ApiModelProperty(required = true, value = "LOA Estimate reference, primary key is ref. here.")
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


  @Override
  public boolean equals(Object o) {
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
        Objects.equals(this.letterOfAcceptanceEstimate, assetsForLOA.letterOfAcceptanceEstimate) &&
        Objects.equals(this.auditDetails, assetsForLOA.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, asset, letterOfAcceptanceEstimate, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetsForLOA {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

