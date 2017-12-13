package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds Asset/Land Details of Abstract Estimate
 */
@ApiModel(description = "An Object that holds Asset/Land Details of Abstract Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class AbstractEstimateAssetDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("abstractEstimate")
  private String abstractEstimate = null;

  @JsonProperty("asset")
  private Asset asset = null;

  @JsonProperty("assetCondition")
  private AssetPresentCondition assetCondition = null;

  @JsonProperty("assetRemarks")
  private String assetRemarks = null;

  @JsonProperty("landAsset")
  private String landAsset = null;

  @JsonProperty("landAssetCondition")
  private LandAssetPresentCondition landAssetCondition = null;

  @JsonProperty("constructionArea")
  private Double constructionArea = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public AbstractEstimateAssetDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Asset/Land Details of Abstract Estimate
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Asset/Land Details of Abstract Estimate")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AbstractEstimateAssetDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Asset/Land Details of Abstract Estimate
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Asset/Land Details of Abstract Estimate")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AbstractEstimateAssetDetail abstractEstimate(String abstractEstimate) {
    this.abstractEstimate = abstractEstimate;
    return this;
  }

   /**
   * Reference to Sanction Asset/Land of Abstract Estimate Object
   * @return abstractEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference to Sanction Asset/Land of Abstract Estimate Object")
  //@NotNull


  public String getAbstractEstimate() {
    return abstractEstimate;
  }

  public void setAbstractEstimate(String abstractEstimate) {
    this.abstractEstimate = abstractEstimate;
  }

  public AbstractEstimateAssetDetail asset(Asset asset) {
    this.asset = asset;
    return this;
  }

   /**
   * Asset for which the Abstract Estimate is created. This field is required and to be shown if Nature of work is 'Repairs or Addition'.
   * @return asset
  **/
  @ApiModelProperty(value = "Asset for which the Abstract Estimate is created. This field is required and to be shown if Nature of work is 'Repairs or Addition'.")

  @Valid

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public AbstractEstimateAssetDetail assetCondition(AssetPresentCondition assetCondition) {
    this.assetCondition = assetCondition;
    return this;
  }

   /**
   * Present condition of the asset. This field is required and to be shown if Nature of work is 'Repairs or Addition'
   * @return assetCondition
  **/
  @ApiModelProperty(value = "Present condition of the asset. This field is required and to be shown if Nature of work is 'Repairs or Addition'")

  @Valid

  public AssetPresentCondition getAssetCondition() {
    return assetCondition;
  }

  public void setAssetCondition(AssetPresentCondition assetCondition) {
    this.assetCondition = assetCondition;
  }

  public AbstractEstimateAssetDetail assetRemarks(String assetRemarks) {
    this.assetRemarks = assetRemarks;
    return this;
  }

   /**
   * Remarks of the asset. This field is required and to be shown if Nature of work is 'Repairs or Addition'
   * @return assetRemarks
  **/
  @ApiModelProperty(value = "Remarks of the asset. This field is required and to be shown if Nature of work is 'Repairs or Addition'")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getAssetRemarks() {
    return assetRemarks;
  }

  public void setAssetRemarks(String assetRemarks) {
    this.assetRemarks = assetRemarks;
  }

  public AbstractEstimateAssetDetail landAsset(String landAsset) {
    this.landAsset = landAsset;
    return this;
  }

   /**
   * Land Asset ID. If Is Land Asset required=Yes then this field is mandatory. This field needs to be shown if Nature of work is 'New'.
   * @return landAsset
  **/
  @ApiModelProperty(value = "Land Asset ID. If Is Land Asset required=Yes then this field is mandatory. This field needs to be shown if Nature of work is 'New'.")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+")
  public String getLandAsset() {
    return landAsset;
  }

  public void setLandAsset(String landAsset) {
    this.landAsset = landAsset;
  }

  public AbstractEstimateAssetDetail landAssetCondition(LandAssetPresentCondition landAssetCondition) {
    this.landAssetCondition = landAssetCondition;
    return this;
  }

   /**
   * Present condition of the asset. If Is Land Asset required=Yes then this field is mandatory. This field needs to be shown if Nature of work is 'New'.
   * @return landAssetCondition
  **/
  @ApiModelProperty(value = "Present condition of the asset. If Is Land Asset required=Yes then this field is mandatory. This field needs to be shown if Nature of work is 'New'.")

  @Valid

  public LandAssetPresentCondition getLandAssetCondition() {
    return landAssetCondition;
  }

  public void setLandAssetCondition(LandAssetPresentCondition landAssetCondition) {
    this.landAssetCondition = landAssetCondition;
  }

  public AbstractEstimateAssetDetail constructionArea(Double constructionArea) {
    this.constructionArea = constructionArea;
    return this;
  }

   /**
   * Construction area. If Is Land Asset required=Yes then this field is mandatory. This field needs to be shown if Nature of work is 'New'.
   * @return constructionArea
  **/
  @ApiModelProperty(value = "Construction area. If Is Land Asset required=Yes then this field is mandatory. This field needs to be shown if Nature of work is 'New'.")


  public Double getConstructionArea() {
    return constructionArea;
  }

  public void setConstructionArea(Double constructionArea) {
    this.constructionArea = constructionArea;
  }

  public AbstractEstimateAssetDetail deleted(Boolean deleted) {
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

  public AbstractEstimateAssetDetail auditDetails(AuditDetails auditDetails) {
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
    AbstractEstimateAssetDetail abstractEstimateAssetDetail = (AbstractEstimateAssetDetail) o;
    return Objects.equals(this.id, abstractEstimateAssetDetail.id) &&
        Objects.equals(this.tenantId, abstractEstimateAssetDetail.tenantId) &&
        Objects.equals(this.abstractEstimate, abstractEstimateAssetDetail.abstractEstimate) &&
        Objects.equals(this.asset, abstractEstimateAssetDetail.asset) &&
        Objects.equals(this.assetCondition, abstractEstimateAssetDetail.assetCondition) &&
        Objects.equals(this.assetRemarks, abstractEstimateAssetDetail.assetRemarks) &&
        Objects.equals(this.landAsset, abstractEstimateAssetDetail.landAsset) &&
        Objects.equals(this.landAssetCondition, abstractEstimateAssetDetail.landAssetCondition) &&
        Objects.equals(this.constructionArea, abstractEstimateAssetDetail.constructionArea) &&
        Objects.equals(this.deleted, abstractEstimateAssetDetail.deleted) &&
        Objects.equals(this.auditDetails, abstractEstimateAssetDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, abstractEstimate, asset, assetCondition, assetRemarks, landAsset, landAssetCondition, constructionArea, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractEstimateAssetDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    abstractEstimate: ").append(toIndentedString(abstractEstimate)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    assetCondition: ").append(toIndentedString(assetCondition)).append("\n");
    sb.append("    assetRemarks: ").append(toIndentedString(assetRemarks)).append("\n");
    sb.append("    landAsset: ").append(toIndentedString(landAsset)).append("\n");
    sb.append("    landAssetCondition: ").append(toIndentedString(landAssetCondition)).append("\n");
    sb.append("    constructionArea: ").append(toIndentedString(constructionArea)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

