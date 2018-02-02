package org.egov.works.measurementbook.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Measurement Book information for the Contractor Bill
 */
@ApiModel(description = "Measurement Book information for the Contractor Bill")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T09:56:01.690Z")

public class MeasurementBookForContractorBill   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("measurementBook")
  private MeasurementBook measurementBook = null;

  @JsonProperty("contractorBill")
  private String contractorBill = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  public MeasurementBookForContractorBill id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the MB for Contractor Bill
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the MB for Contractor Bill")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MeasurementBookForContractorBill tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the MB for Contractor Bill
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the MB for Contractor Bill")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MeasurementBookForContractorBill measurementBook(MeasurementBook measurementBook) {
    this.measurementBook = measurementBook;
    return this;
  }

   /**
   * Measurement Book Reference in the MB for Contractor Bill
   * @return measurementBook
  **/
  @ApiModelProperty(value = "Measurement Book Reference in the MB for Contractor Bill")

  //@Valid

  public MeasurementBook getMeasurementBook() {
    return measurementBook;
  }

  public void setMeasurementBook(MeasurementBook measurementBook) {
    this.measurementBook = measurementBook;
  }

  public MeasurementBookForContractorBill contractorBill(String contractorBill) {
    this.contractorBill = contractorBill;
    return this;
  }

   /**
   * Bill reference in the MB for Contractor Bill
   * @return contractorBill
  **/
  @ApiModelProperty(required = true, value = "Bill reference in the MB for Contractor Bill")
//  @NotNull


  public String getContractorBill() {
    return contractorBill;
  }

  public void setContractorBill(String contractorBill) {
    this.contractorBill = contractorBill;
  }

  public MeasurementBookForContractorBill auditDetails(AuditDetails auditDetails) {
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

  public MeasurementBookForContractorBill deleted(Boolean deleted) {
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
    MeasurementBookForContractorBill measurementBookForContractorBill = (MeasurementBookForContractorBill) o;
    return Objects.equals(this.id, measurementBookForContractorBill.id) &&
        Objects.equals(this.tenantId, measurementBookForContractorBill.tenantId) &&
        Objects.equals(this.measurementBook, measurementBookForContractorBill.measurementBook) &&
        Objects.equals(this.contractorBill, measurementBookForContractorBill.contractorBill) &&
        Objects.equals(this.auditDetails, measurementBookForContractorBill.auditDetails) &&
        Objects.equals(this.deleted, measurementBookForContractorBill.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, measurementBook, contractorBill, auditDetails, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MeasurementBookForContractorBill {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    measurementBook: ").append(toIndentedString(measurementBook)).append("\n");
    sb.append("    contractorBill: ").append(toIndentedString(contractorBill)).append("\n");
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

