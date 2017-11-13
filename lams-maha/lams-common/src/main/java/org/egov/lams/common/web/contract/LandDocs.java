package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This object holds list of documents attached during the transactions on Land
 */
@ApiModel(description = "This object holds list of documents attached during the transactions on Land")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class LandDocs   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("agreement")
  private LandRegister agreement = null;

  @JsonProperty("documentType")
  private DocumentType documentType = null;

  @JsonProperty("fileStore")
  private String fileStore = null;

  public LandDocs id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the LandDocs.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the LandDocs.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandDocs tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the LandDocs
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenant id of the LandDocs")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandDocs agreement(LandRegister agreement) {
    this.agreement = agreement;
    return this;
  }

   /**
   * Get agreement
   * @return agreement
  **/
  @ApiModelProperty(value = "")

  @Valid

  public LandRegister getAgreement() {
    return agreement;
  }

  public void setAgreement(LandRegister agreement) {
    this.agreement = agreement;
  }

  public LandDocs documentType(DocumentType documentType) {
    this.documentType = documentType;
    return this;
  }

   /**
   * Get documentType
   * @return documentType
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DocumentType getDocumentType() {
    return documentType;
  }

  public void setDocumentType(DocumentType documentType) {
    this.documentType = documentType;
  }

  public LandDocs fileStore(String fileStore) {
    this.fileStore = fileStore;
    return this;
  }

   /**
   * File store reference key.
   * @return fileStore
  **/
  @ApiModelProperty(value = "File store reference key.")


  public String getFileStore() {
    return fileStore;
  }

  public void setFileStore(String fileStore) {
    this.fileStore = fileStore;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandDocs landDocs = (LandDocs) o;
    return Objects.equals(this.id, landDocs.id) &&
        Objects.equals(this.tenantId, landDocs.tenantId) &&
        Objects.equals(this.agreement, landDocs.agreement) &&
        Objects.equals(this.documentType, landDocs.documentType) &&
        Objects.equals(this.fileStore, landDocs.fileStore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, agreement, documentType, fileStore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandDocs {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    agreement: ").append(toIndentedString(agreement)).append("\n");
    sb.append("    documentType: ").append(toIndentedString(documentType)).append("\n");
    sb.append("    fileStore: ").append(toIndentedString(fileStore)).append("\n");
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

