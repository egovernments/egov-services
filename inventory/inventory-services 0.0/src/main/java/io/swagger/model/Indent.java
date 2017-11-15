package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.IndentDetail;
import io.swagger.model.Store;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class Indent   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("issueStore")
  private Store issueStore = null;

  @JsonProperty("indentDate")
  private Long indentDate = null;

  @JsonProperty("indentNumber")
  private String indentNumber = null;

  /**
   * indent purpose of the Indent 
   */
  public enum IndentPurposeEnum {
    CONSUMPTION("Consumption"),
    
    REPAIRSANDMAINTENANCE("RepairsAndMaintenance"),
    
    CAPITAL("Capital"),
    
    MATERIALTRANSFERNOTE("MaterialTransferNote");

    private String value;

    IndentPurposeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static IndentPurposeEnum fromValue(String text) {
      for (IndentPurposeEnum b : IndentPurposeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("indentPurpose")
  private IndentPurposeEnum indentPurpose = null;

  @JsonProperty("description")
  private String description = null;

  /**
   * indent status of the Indent 
   */
  public enum IndentStatusEnum {
    CREATED("CREATED"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED"),
    
    CANCELED("CANCELED");

    private String value;

    IndentStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static IndentStatusEnum fromValue(String text) {
      for (IndentStatusEnum b : IndentStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("indentStatus")
  private IndentStatusEnum indentStatus = null;

  @JsonProperty("indentMaterials")
  @Valid
  private List<IndentDetail> indentMaterials = new ArrayList<IndentDetail>();

  @JsonProperty("totalIndentValue")
  private BigDecimal totalIndentValue = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Indent id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Indent 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Indent ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Indent tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Indent
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Indent")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Indent issueStore(Store issueStore) {
    this.issueStore = issueStore;
    return this;
  }

   /**
   * Get issueStore
   * @return issueStore
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Store getIssueStore() {
    return issueStore;
  }

  public void setIssueStore(Store issueStore) {
    this.issueStore = issueStore;
  }

  public Indent indentDate(Long indentDate) {
    this.indentDate = indentDate;
    return this;
  }

   /**
   * indent date of the Indent 
   * @return indentDate
  **/
  @ApiModelProperty(required = true, value = "indent date of the Indent ")
  @NotNull


  public Long getIndentDate() {
    return indentDate;
  }

  public void setIndentDate(Long indentDate) {
    this.indentDate = indentDate;
  }

  public Indent indentNumber(String indentNumber) {
    this.indentNumber = indentNumber;
    return this;
  }

   /**
   * indentNumber  Auto generated number, read only <ULB short code><Store Code><fin. Year><serial No.> 
   * @return indentNumber
  **/
  @ApiModelProperty(readOnly = true, value = "indentNumber  Auto generated number, read only <ULB short code><Store Code><fin. Year><serial No.> ")

 @Size(max=100)
  public String getIndentNumber() {
    return indentNumber;
  }

  public void setIndentNumber(String indentNumber) {
    this.indentNumber = indentNumber;
  }

  public Indent indentPurpose(IndentPurposeEnum indentPurpose) {
    this.indentPurpose = indentPurpose;
    return this;
  }

   /**
   * indent purpose of the Indent 
   * @return indentPurpose
  **/
  @ApiModelProperty(required = true, value = "indent purpose of the Indent ")
  @NotNull


  public IndentPurposeEnum getIndentPurpose() {
    return indentPurpose;
  }

  public void setIndentPurpose(IndentPurposeEnum indentPurpose) {
    this.indentPurpose = indentPurpose;
  }

  public Indent description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the Indent 
   * @return description
  **/
  @ApiModelProperty(value = "description of the Indent ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Indent indentStatus(IndentStatusEnum indentStatus) {
    this.indentStatus = indentStatus;
    return this;
  }

   /**
   * indent status of the Indent 
   * @return indentStatus
  **/
  @ApiModelProperty(value = "indent status of the Indent ")


  public IndentStatusEnum getIndentStatus() {
    return indentStatus;
  }

  public void setIndentStatus(IndentStatusEnum indentStatus) {
    this.indentStatus = indentStatus;
  }

  public Indent indentMaterials(List<IndentDetail> indentMaterials) {
    this.indentMaterials = indentMaterials;
    return this;
  }

  public Indent addIndentMaterialsItem(IndentDetail indentMaterialsItem) {
    this.indentMaterials.add(indentMaterialsItem);
    return this;
  }

   /**
   * Get indentMaterials
   * @return indentMaterials
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<IndentDetail> getIndentMaterials() {
    return indentMaterials;
  }

  public void setIndentMaterials(List<IndentDetail> indentMaterials) {
    this.indentMaterials = indentMaterials;
  }

  public Indent totalIndentValue(BigDecimal totalIndentValue) {
    this.totalIndentValue = totalIndentValue;
    return this;
  }

   /**
   * totalIndentValue  denormalized value from Indent Material 
   * @return totalIndentValue
  **/
  @ApiModelProperty(value = "totalIndentValue  denormalized value from Indent Material ")

  @Valid

  public BigDecimal getTotalIndentValue() {
    return totalIndentValue;
  }

  public void setTotalIndentValue(BigDecimal totalIndentValue) {
    this.totalIndentValue = totalIndentValue;
  }

  public Indent fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Indent Note/Transfer Indent Generated 
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Indent Note/Transfer Indent Generated ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public Indent designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Designation of the employee at the time of Indent Note/Transfer Indent creation
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the employee at the time of Indent Note/Transfer Indent creation")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public Indent auditDetails(AuditDetails auditDetails) {
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
    Indent indent = (Indent) o;
    return Objects.equals(this.id, indent.id) &&
        Objects.equals(this.tenantId, indent.tenantId) &&
        Objects.equals(this.issueStore, indent.issueStore) &&
        Objects.equals(this.indentDate, indent.indentDate) &&
        Objects.equals(this.indentNumber, indent.indentNumber) &&
        Objects.equals(this.indentPurpose, indent.indentPurpose) &&
        Objects.equals(this.description, indent.description) &&
        Objects.equals(this.indentStatus, indent.indentStatus) &&
        Objects.equals(this.indentMaterials, indent.indentMaterials) &&
        Objects.equals(this.totalIndentValue, indent.totalIndentValue) &&
        Objects.equals(this.fileStoreId, indent.fileStoreId) &&
        Objects.equals(this.designation, indent.designation) &&
        Objects.equals(this.auditDetails, indent.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, issueStore, indentDate, indentNumber, indentPurpose, description, indentStatus, indentMaterials, totalIndentValue, fileStoreId, designation, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Indent {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    issueStore: ").append(toIndentedString(issueStore)).append("\n");
    sb.append("    indentDate: ").append(toIndentedString(indentDate)).append("\n");
    sb.append("    indentNumber: ").append(toIndentedString(indentNumber)).append("\n");
    sb.append("    indentPurpose: ").append(toIndentedString(indentPurpose)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    indentStatus: ").append(toIndentedString(indentStatus)).append("\n");
    sb.append("    indentMaterials: ").append(toIndentedString(indentMaterials)).append("\n");
    sb.append("    totalIndentValue: ").append(toIndentedString(totalIndentValue)).append("\n");
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

