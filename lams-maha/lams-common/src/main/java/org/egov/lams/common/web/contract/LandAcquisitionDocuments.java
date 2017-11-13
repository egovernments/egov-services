package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object to hold the support document detail for a given land acquisiton
 */
@ApiModel(description = "An Object to hold the support document detail for a given land acquisiton")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class LandAcquisitionDocuments   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("landAcquisition")
  private LandAcquisition landAcquisition = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("comments")
  private String comments = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public LandAcquisitionDocuments id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Support Document
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Support Document")

 @Size(max=64)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandAcquisitionDocuments tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the land acquisiton
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the land acquisiton")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandAcquisitionDocuments landAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
    return this;
  }

   /**
   * Use  land acquisition id.Unique Identifier of the land acquisiton
   * @return landAcquisition
  **/
  @ApiModelProperty(required = true, value = "Use  land acquisition id.Unique Identifier of the land acquisiton")
  @NotNull

  @Valid

  public LandAcquisition getLandAcquisition() {
    return landAcquisition;
  }

  public void setLandAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
  }

  public LandAcquisitionDocuments fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * Unique Identifier of the FileStore
   * @return fileStoreId
  **/
  @ApiModelProperty(required = true, value = "Unique Identifier of the FileStore")
  @NotNull


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public LandAcquisitionDocuments comments(String comments) {
    this.comments = comments;
    return this;
  }

   /**
   * Get comments
   * @return comments
  **/
  @ApiModelProperty(value = "")

 @Size(max=1024)
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public LandAcquisitionDocuments auditDetails(AuditDetails auditDetails) {
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
    LandAcquisitionDocuments landAcquisitionDocuments = (LandAcquisitionDocuments) o;
    return Objects.equals(this.id, landAcquisitionDocuments.id) &&
        Objects.equals(this.tenantId, landAcquisitionDocuments.tenantId) &&
        Objects.equals(this.landAcquisition, landAcquisitionDocuments.landAcquisition) &&
        Objects.equals(this.fileStoreId, landAcquisitionDocuments.fileStoreId) &&
        Objects.equals(this.comments, landAcquisitionDocuments.comments) &&
        Objects.equals(this.auditDetails, landAcquisitionDocuments.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, landAcquisition, fileStoreId, comments, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandAcquisitionDocuments {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    landAcquisition: ").append(toIndentedString(landAcquisition)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
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

