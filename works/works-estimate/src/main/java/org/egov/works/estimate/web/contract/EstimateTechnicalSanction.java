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
 * An Object that holds the basic data of Technical Sanction for Detailed Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Technical Sanction for Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class EstimateTechnicalSanction   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("technicalSanctionNumber")
  private String technicalSanctionNumber = null;

  @JsonProperty("detailedEstimate")
  private String detailedEstimate = null;

  @JsonProperty("technicalSanctionDate")
  private Long technicalSanctionDate = null;

  @JsonProperty("technicalSanctionBy")
  private User technicalSanctionBy = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public EstimateTechnicalSanction id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Estimate Technical Sanction
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Estimate Technical Sanction")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstimateTechnicalSanction tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Estimate Technical Sanction
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Estimate Technical Sanction")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstimateTechnicalSanction technicalSanctionNumber(String technicalSanctionNumber) {
    this.technicalSanctionNumber = technicalSanctionNumber;
    return this;
  }

   /**
   * Technical Sanction Number of the Detailed Estimate
   * @return technicalSanctionNumber
  **/
  @ApiModelProperty(required = true, value = "Technical Sanction Number of the Detailed Estimate")
  //@NotNull

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(min=1,max=50)
  public String getTechnicalSanctionNumber() {
    return technicalSanctionNumber;
  }

  public void setTechnicalSanctionNumber(String technicalSanctionNumber) {
    this.technicalSanctionNumber = technicalSanctionNumber;
  }

  public EstimateTechnicalSanction detailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
    return this;
  }

   /**
   * Reference of the Detailed Estimate for which the Technical sanction belongs to
   * @return detailedEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference of the Detailed Estimate for which the Technical sanction belongs to")
  //@NotNull


  public String getDetailedEstimate() {
    return detailedEstimate;
  }

  public void setDetailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
  }

  public EstimateTechnicalSanction technicalSanctionDate(Long technicalSanctionDate) {
    this.technicalSanctionDate = technicalSanctionDate;
    return this;
  }

   /**
   * Epoch time of the Technical Sanction Date. Technical Sanctioned date should be on or after the Detailed Estimate date.
   * @return technicalSanctionDate
  **/
  @ApiModelProperty(required = true, value = "Epoch time of the Technical Sanction Date. Technical Sanctioned date should be on or after the Detailed Estimate date.")
  //@NotNull


  public Long getTechnicalSanctionDate() {
    return technicalSanctionDate;
  }

  public void setTechnicalSanctionDate(Long technicalSanctionDate) {
    this.technicalSanctionDate = technicalSanctionDate;
  }

  public EstimateTechnicalSanction technicalSanctionBy(User technicalSanctionBy) {
    this.technicalSanctionBy = technicalSanctionBy;
    return this;
  }

   /**
   * User who technical sanctioned Detailed Estimate
   * @return technicalSanctionBy
  **/
  @ApiModelProperty(required = true, value = "User who technical sanctioned Detailed Estimate")
  @NotNull

  //@Valid

  public User getTechnicalSanctionBy() {
    return technicalSanctionBy;
  }

  public void setTechnicalSanctionBy(User technicalSanctionBy) {
    this.technicalSanctionBy = technicalSanctionBy;
  }

  public EstimateTechnicalSanction deleted(Boolean deleted) {
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

  public EstimateTechnicalSanction auditDetails(AuditDetails auditDetails) {
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
    EstimateTechnicalSanction estimateTechnicalSanction = (EstimateTechnicalSanction) o;
    return Objects.equals(this.id, estimateTechnicalSanction.id) &&
        Objects.equals(this.tenantId, estimateTechnicalSanction.tenantId) &&
        Objects.equals(this.technicalSanctionNumber, estimateTechnicalSanction.technicalSanctionNumber) &&
        Objects.equals(this.detailedEstimate, estimateTechnicalSanction.detailedEstimate) &&
        Objects.equals(this.technicalSanctionDate, estimateTechnicalSanction.technicalSanctionDate) &&
        Objects.equals(this.technicalSanctionBy, estimateTechnicalSanction.technicalSanctionBy) &&
        Objects.equals(this.deleted, estimateTechnicalSanction.deleted) &&
        Objects.equals(this.auditDetails, estimateTechnicalSanction.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, technicalSanctionNumber, detailedEstimate, technicalSanctionDate, technicalSanctionBy, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateTechnicalSanction {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    technicalSanctionNumber: ").append(toIndentedString(technicalSanctionNumber)).append("\n");
    sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
    sb.append("    technicalSanctionDate: ").append(toIndentedString(technicalSanctionDate)).append("\n");
    sb.append("    technicalSanctionBy: ").append(toIndentedString(technicalSanctionBy)).append("\n");
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

