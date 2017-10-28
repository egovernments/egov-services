package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Store;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class MaterialIssue   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("store")
  private Store store = null;

  @JsonProperty("issueNoteNumber")
  private String issueNoteNumber = null;

  @JsonProperty("issueDate")
  private Long issueDate = null;

  /**
   * material issue status of the MaterialIssue 
   */
  public enum MaterialIssueStatusEnum {
    CREATED("CREATED"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED"),
    
    CANCELED("CANCELED");

    private String value;

    MaterialIssueStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MaterialIssueStatusEnum fromValue(String text) {
      for (MaterialIssueStatusEnum b : MaterialIssueStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("materialIssueStatus")
  private MaterialIssueStatusEnum materialIssueStatus = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("totalIssueValue")
  private BigDecimal totalIssueValue = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public MaterialIssue id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Material Issue 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Material Issue ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MaterialIssue tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Issue
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Material Issue")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MaterialIssue store(Store store) {
    this.store = store;
    return this;
  }

   /**
   * Get store
   * @return store
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public MaterialIssue issueNoteNumber(String issueNoteNumber) {
    this.issueNoteNumber = issueNoteNumber;
    return this;
  }

   /**
   * issueNoteNumber  Auto generated number, read only 
   * @return issueNoteNumber
  **/
  @ApiModelProperty(readOnly = true, value = "issueNoteNumber  Auto generated number, read only ")

 @Size(max=100)
  public String getIssueNoteNumber() {
    return issueNoteNumber;
  }

  public void setIssueNoteNumber(String issueNoteNumber) {
    this.issueNoteNumber = issueNoteNumber;
  }

  public MaterialIssue issueDate(Long issueDate) {
    this.issueDate = issueDate;
    return this;
  }

   /**
   * issue date of the MaterialIssue 
   * @return issueDate
  **/
  @ApiModelProperty(required = true, value = "issue date of the MaterialIssue ")
  @NotNull


  public Long getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Long issueDate) {
    this.issueDate = issueDate;
  }

  public MaterialIssue materialIssueStatus(MaterialIssueStatusEnum materialIssueStatus) {
    this.materialIssueStatus = materialIssueStatus;
    return this;
  }

   /**
   * material issue status of the MaterialIssue 
   * @return materialIssueStatus
  **/
  @ApiModelProperty(value = "material issue status of the MaterialIssue ")

 @Size(min=5,max=50)
  public MaterialIssueStatusEnum getMaterialIssueStatus() {
    return materialIssueStatus;
  }

  public void setMaterialIssueStatus(MaterialIssueStatusEnum materialIssueStatus) {
    this.materialIssueStatus = materialIssueStatus;
  }

  public MaterialIssue description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the MaterialIssue 
   * @return description
  **/
  @ApiModelProperty(value = "description of the MaterialIssue ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MaterialIssue totalIssueValue(BigDecimal totalIssueValue) {
    this.totalIssueValue = totalIssueValue;
    return this;
  }

   /**
   * total issue value of the MaterialIssue 
   * @return totalIssueValue
  **/
  @ApiModelProperty(value = "total issue value of the MaterialIssue ")

  @Valid

  public BigDecimal getTotalIssueValue() {
    return totalIssueValue;
  }

  public void setTotalIssueValue(BigDecimal totalIssueValue) {
    this.totalIssueValue = totalIssueValue;
  }

  public MaterialIssue fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Indent Issue.     
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Indent Issue.     ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public MaterialIssue designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Designation of the of the created by user at the time of Indent Issue/Non Indent Issue created.
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the of the created by user at the time of Indent Issue/Non Indent Issue created.")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public MaterialIssue auditDetails(AuditDetails auditDetails) {
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
    MaterialIssue materialIssue = (MaterialIssue) o;
    return Objects.equals(this.id, materialIssue.id) &&
        Objects.equals(this.tenantId, materialIssue.tenantId) &&
        Objects.equals(this.store, materialIssue.store) &&
        Objects.equals(this.issueNoteNumber, materialIssue.issueNoteNumber) &&
        Objects.equals(this.issueDate, materialIssue.issueDate) &&
        Objects.equals(this.materialIssueStatus, materialIssue.materialIssueStatus) &&
        Objects.equals(this.description, materialIssue.description) &&
        Objects.equals(this.totalIssueValue, materialIssue.totalIssueValue) &&
        Objects.equals(this.fileStoreId, materialIssue.fileStoreId) &&
        Objects.equals(this.designation, materialIssue.designation) &&
        Objects.equals(this.auditDetails, materialIssue.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, store, issueNoteNumber, issueDate, materialIssueStatus, description, totalIssueValue, fileStoreId, designation, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialIssue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    store: ").append(toIndentedString(store)).append("\n");
    sb.append("    issueNoteNumber: ").append(toIndentedString(issueNoteNumber)).append("\n");
    sb.append("    issueDate: ").append(toIndentedString(issueDate)).append("\n");
    sb.append("    materialIssueStatus: ").append(toIndentedString(materialIssueStatus)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    totalIssueValue: ").append(toIndentedString(totalIssueValue)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
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

