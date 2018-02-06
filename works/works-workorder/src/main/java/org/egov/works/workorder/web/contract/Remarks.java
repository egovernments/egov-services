package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds Remarks Master data
 */
@ApiModel(description = "An Object that holds Remarks Master data")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-10T13:23:45.489Z")

public class Remarks {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("typeOfDocument")
  private TypeOfDocument typeOfDocument = null;

  @JsonProperty("remarksType")
  private RemarksType remarksType = null;

  @JsonProperty("remarksDetails")
  private List<RemarksDetail> remarksDetails = new ArrayList<RemarksDetail>();

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Remarks id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Remarks Master
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Remarks Master")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Remarks tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Remarks Master
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Remarks Master")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Remarks typeOfDocument(TypeOfDocument typeOfDocument) {
    this.typeOfDocument = typeOfDocument;
    return this;
  }

   /**
   * Type Of Document of the Remarks Master
   * @return typeOfDocument
  **/
  @ApiModelProperty(required = true, value = "Type Of Document of the Remarks Master")
  @NotNull

  @Valid

  public TypeOfDocument getTypeOfDocument() {
    return typeOfDocument;
  }

  public void setTypeOfDocument(TypeOfDocument typeOfDocument) {
    this.typeOfDocument = typeOfDocument;
  }

  public Remarks remarksType(RemarksType remarksType) {
    this.remarksType = remarksType;
    return this;
  }

   /**
   * Remarks Type of the Remarks Master
   * @return remarksType
  **/
  @ApiModelProperty(required = true, value = "Remarks Type of the Remarks Master")
  @NotNull

  @Valid

  public RemarksType getRemarksType() {
    return remarksType;
  }

  public void setRemarksType(RemarksType remarksType) {
    this.remarksType = remarksType;
  }

  public Remarks remarksDetails(List<RemarksDetail> remarksDetails) {
    this.remarksDetails = remarksDetails;
    return this;
  }

  public Remarks addRemarksDetailsItem(RemarksDetail remarksDetailsItem) {
    this.remarksDetails.add(remarksDetailsItem);
    return this;
  }

   /**
   * Array of Remarks Details
   * @return remarksDetails
  **/
  @ApiModelProperty(required = true, value = "Array of Remarks Details")
  @NotNull

  @Valid
 @Size(min=1)
  public List<RemarksDetail> getRemarksDetails() {
    return remarksDetails;
  }

  public void setRemarksDetails(List<RemarksDetail> remarksDetails) {
    this.remarksDetails = remarksDetails;
  }

  public Remarks deleted(Boolean deleted) {
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

  public Remarks auditDetails(AuditDetails auditDetails) {
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
    Remarks remarks = (Remarks) o;
    return Objects.equals(this.id, remarks.id) &&
        Objects.equals(this.tenantId, remarks.tenantId) &&
        Objects.equals(this.typeOfDocument, remarks.typeOfDocument) &&
        Objects.equals(this.remarksType, remarks.remarksType) &&
        Objects.equals(this.remarksDetails, remarks.remarksDetails) &&
        Objects.equals(this.deleted, remarks.deleted) &&
        Objects.equals(this.auditDetails, remarks.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, typeOfDocument, remarksType, remarksDetails, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Remarks {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    typeOfDocument: ").append(toIndentedString(typeOfDocument)).append("\n");
    sb.append("    remarksType: ").append(toIndentedString(remarksType)).append("\n");
    sb.append("    remarksDetails: ").append(toIndentedString(remarksDetails)).append("\n");
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

