package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This object holds list of documents attached during the transacitons on agreement
 */
@ApiModel(description = "This object holds list of documents attached during the transacitons on agreement")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class AgreementDocs   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("agreement")
  private Agreement agreement = null;

  @JsonProperty("documentType")
  private DocumentType documentType = null;

  @JsonProperty("fileStore")
  private String fileStore = null;

  public AgreementDocs id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the AgreementDocs.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the AgreementDocs.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AgreementDocs tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the AgreementDocs
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenant id of the AgreementDocs")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AgreementDocs agreement(Agreement agreement) {
    this.agreement = agreement;
    return this;
  }

   /**
   * Get agreement
   * @return agreement
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Agreement getAgreement() {
    return agreement;
  }

  public void setAgreement(Agreement agreement) {
    this.agreement = agreement;
  }

  public AgreementDocs documentType(DocumentType documentType) {
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

  public AgreementDocs fileStore(String fileStore) {
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
    AgreementDocs agreementDocs = (AgreementDocs) o;
    return Objects.equals(this.id, agreementDocs.id) &&
        Objects.equals(this.tenantId, agreementDocs.tenantId) &&
        Objects.equals(this.agreement, agreementDocs.agreement) &&
        Objects.equals(this.documentType, agreementDocs.documentType) &&
        Objects.equals(this.fileStore, agreementDocs.fileStore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, agreement, documentType, fileStore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AgreementDocs {\n");
    
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

