package org.egov.tradelicense.domain.model;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SupportDocument   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("licenseId")
  private Long licenseId = null;

  @JsonProperty("documentTypeId")
  private Long documentTypeId = null;

  @JsonProperty("fileStoreId")
  private Long fileStoreId = null;

  @JsonProperty("comments")
  private String comments = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public SupportDocument id(Long id) {
    this.id = id;
    return this;
  }



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SupportDocument licenseId(Long licenseId) {
    this.licenseId = licenseId;
    return this;
  }


  public Long getLicenseId() {
    return licenseId;
  }

  public void setLicenseId(Long licenseId) {
    this.licenseId = licenseId;
  }

  public SupportDocument documentTypeId(Long documentTypeId) {
    this.documentTypeId = documentTypeId;
    return this;
  }

  public Long getDocumentTypeId() {
    return documentTypeId;
  }

  public void setDocumentTypeId(Long documentTypeId) {
    this.documentTypeId = documentTypeId;
  }

  public SupportDocument fileStoreId(Long fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

  public Long getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(Long fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public SupportDocument comments(String comments) {
    this.comments = comments;
    return this;
  }


 @Size(max=1024)
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public SupportDocument auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

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
    SupportDocument supportDocument = (SupportDocument) o;
    return Objects.equals(this.id, supportDocument.id) &&
        Objects.equals(this.licenseId, supportDocument.licenseId) &&
        Objects.equals(this.documentTypeId, supportDocument.documentTypeId) &&
        Objects.equals(this.fileStoreId, supportDocument.fileStoreId) &&
        Objects.equals(this.comments, supportDocument.comments) &&
        Objects.equals(this.auditDetails, supportDocument.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, licenseId, documentTypeId, fileStoreId, comments, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupportDocument {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    licenseId: ").append(toIndentedString(licenseId)).append("\n");
    sb.append("    documentTypeId: ").append(toIndentedString(documentTypeId)).append("\n");
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

