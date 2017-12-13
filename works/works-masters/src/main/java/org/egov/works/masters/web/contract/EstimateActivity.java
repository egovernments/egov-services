package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object holds the basic data of Estimate Activity
 */
@ApiModel(description = "An Object holds the basic data of Estimate Activity")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class EstimateActivity   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("scheduleOfRate")
  private ScheduleOfRate scheduleOfRate = null;

  @JsonProperty("nonSor")
  private NonSOR nonSor = null;

  @JsonProperty("uom")
  private UOM uom = null;

  @JsonProperty("unitRate")
  private BigDecimal unitRate = null;

  @JsonProperty("estimateRate")
  private BigDecimal estimateRate = null;

  @JsonProperty("quantity")
  private Double quantity = null;

  @JsonProperty("serviceTaxPerc")
  private Double serviceTaxPerc = null;

  @JsonProperty("revisionType")
  private RevisionType revisionType = null;

  @JsonProperty("parent")
  private String parent = null;

  @JsonProperty("detailedEstimate")
  private String detailedEstimate = null;

  @JsonProperty("estimateMeasurementSheets")
  private List<EstimateMeasurementSheet> estimateMeasurementSheets = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;
  
  @JsonProperty("deleted")
  private Boolean deleted = null;

  public EstimateActivity id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Estimate Activity
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Estimate Activity")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstimateActivity tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Estimate Activity
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Estimate Activity")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstimateActivity scheduleOfRate(ScheduleOfRate scheduleOfRate) {
    this.scheduleOfRate = scheduleOfRate;
    return this;
  }

   /**
   * Schedule Of Rate reference of the Estimate Activity. Either Schedule Of Rate or Non SOR is mandatory for any activity.
   * @return scheduleOfRate
  **/
  @ApiModelProperty(value = "Schedule Of Rate reference of the Estimate Activity. Either Schedule Of Rate or Non SOR is mandatory for any activity.")

  @Valid

  public ScheduleOfRate getScheduleOfRate() {
    return scheduleOfRate;
  }

  public void setScheduleOfRate(ScheduleOfRate scheduleOfRate) {
    this.scheduleOfRate = scheduleOfRate;
  }

  public EstimateActivity nonSor(NonSOR nonSor) {
    this.nonSor = nonSor;
    return this;
  }

   /**
   * Schedule Of Rate reference of the Estimate Activity. Either Schedule Of Rate or Non SOR is mandatory for any activity.
   * @return nonSor
  **/
  @ApiModelProperty(value = "Schedule Of Rate reference of the Estimate Activity. Either Schedule Of Rate or Non SOR is mandatory for any activity.")

  @Valid

  public NonSOR getNonSor() {
    return nonSor;
  }

  public void setNonSor(NonSOR nonSor) {
    this.nonSor = nonSor;
  }

  public EstimateActivity uom(UOM uom) {
    this.uom = uom;
    return this;
  }

   /**
   * UOM for the Estimate Activity
   * @return uom
  **/
  @ApiModelProperty(required = true, value = "UOM for the Estimate Activity")
  @NotNull

  @Valid

  public UOM getUom() {
    return uom;
  }

  public void setUom(UOM uom) {
    this.uom = uom;
  }

  public EstimateActivity unitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
    return this;
  }

   /**
   * Unit Rate of the Estimate Activity
   * @return unitRate
  **/
  @ApiModelProperty(required = true, value = "Unit Rate of the Estimate Activity")
  @NotNull

  @Valid

  public BigDecimal getUnitRate() {
    return unitRate;
  }

  public void setUnitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
  }

  public EstimateActivity estimateRate(BigDecimal estimateRate) {
    this.estimateRate = estimateRate;
    return this;
  }

   /**
   * Estimate Rate of the Estimate Activity
   * @return estimateRate
  **/
  @ApiModelProperty(required = true, value = "Estimate Rate of the Estimate Activity")
  @NotNull

  @Valid

  public BigDecimal getEstimateRate() {
    return estimateRate;
  }

  public void setEstimateRate(BigDecimal estimateRate) {
    this.estimateRate = estimateRate;
  }

  public EstimateActivity quantity(Double quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Quantity of the Estimate Activity
   * @return quantity
  **/
  @ApiModelProperty(required = true, value = "Quantity of the Estimate Activity")
  @NotNull


  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public EstimateActivity serviceTaxPerc(Double serviceTaxPerc) {
    this.serviceTaxPerc = serviceTaxPerc;
    return this;
  }

   /**
   * Service Tax or VAT Percentage of the Estimate Activity
   * @return serviceTaxPerc
  **/
  @ApiModelProperty(value = "Service Tax or VAT Percentage of the Estimate Activity")


  public Double getServiceTaxPerc() {
    return serviceTaxPerc;
  }

  public void setServiceTaxPerc(Double serviceTaxPerc) {
    this.serviceTaxPerc = serviceTaxPerc;
  }

  public EstimateActivity revisionType(RevisionType revisionType) {
    this.revisionType = revisionType;
    return this;
  }

   /**
   * Revision Type for the Revision Estimate Activity
   * @return revisionType
  **/
  @ApiModelProperty(value = "Revision Type for the Revision Estimate Activity")

  @Valid

  public RevisionType getRevisionType() {
    return revisionType;
  }

  public void setRevisionType(RevisionType revisionType) {
    this.revisionType = revisionType;
  }

  public EstimateActivity parent(String parent) {
    this.parent = parent;
    return this;
  }

   /**
   * Parent Activity in case of Revision Estimate Activity (Additional/Reduced Quantity). Ref. is pk here.
   * @return parent
  **/
  @ApiModelProperty(value = "Parent Activity in case of Revision Estimate Activity (Additional/Reduced Quantity). Ref. is pk here.")


  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public EstimateActivity detailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
    return this;
  }

   /**
   * Reference of the Detailed Estimate for Estimate Activity
   * @return detailedEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference of the Detailed Estimate for Estimate Activity")
  @NotNull


  public String getDetailedEstimate() {
    return detailedEstimate;
  }

  public void setDetailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
  }

  public EstimateActivity estimateMeasurementSheets(List<EstimateMeasurementSheet> estimateMeasurementSheets) {
    this.estimateMeasurementSheets = estimateMeasurementSheets;
    return this;
  }

  public EstimateActivity addEstimateMeasurementSheetsItem(EstimateMeasurementSheet estimateMeasurementSheetsItem) {
    if (this.estimateMeasurementSheets == null) {
      this.estimateMeasurementSheets = new ArrayList<EstimateMeasurementSheet>();
    }
    this.estimateMeasurementSheets.add(estimateMeasurementSheetsItem);
    return this;
  }

   /**
   * Measurement sheet list for the Estimate Activity
   * @return estimateMeasurementSheets
  **/
  @ApiModelProperty(value = "Measurement sheet list for the Estimate Activity")

  @Valid

  public List<EstimateMeasurementSheet> getEstimateMeasurementSheets() {
    return estimateMeasurementSheets;
  }

  public void setEstimateMeasurementSheets(List<EstimateMeasurementSheet> estimateMeasurementSheets) {
    this.estimateMeasurementSheets = estimateMeasurementSheets;
  }

  public EstimateActivity auditDetails(AuditDetails auditDetails) {
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


  public Boolean getDeleted() {
	return deleted;
}

public void setDeleted(Boolean deleted) {
	this.deleted = deleted;
}

@Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EstimateActivity estimateActivity = (EstimateActivity) o;
    return Objects.equals(this.id, estimateActivity.id) &&
        Objects.equals(this.tenantId, estimateActivity.tenantId) &&
        Objects.equals(this.scheduleOfRate, estimateActivity.scheduleOfRate) &&
        Objects.equals(this.nonSor, estimateActivity.nonSor) &&
        Objects.equals(this.uom, estimateActivity.uom) &&
        Objects.equals(this.unitRate, estimateActivity.unitRate) &&
        Objects.equals(this.estimateRate, estimateActivity.estimateRate) &&
        Objects.equals(this.quantity, estimateActivity.quantity) &&
        Objects.equals(this.serviceTaxPerc, estimateActivity.serviceTaxPerc) &&
        Objects.equals(this.revisionType, estimateActivity.revisionType) &&
        Objects.equals(this.parent, estimateActivity.parent) &&
        Objects.equals(this.detailedEstimate, estimateActivity.detailedEstimate) &&
        Objects.equals(this.estimateMeasurementSheets, estimateActivity.estimateMeasurementSheets) &&
        Objects.equals(this.auditDetails, estimateActivity.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, scheduleOfRate, nonSor, uom, unitRate, estimateRate, quantity, serviceTaxPerc, revisionType, parent, detailedEstimate, estimateMeasurementSheets, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateActivity {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    scheduleOfRate: ").append(toIndentedString(scheduleOfRate)).append("\n");
    sb.append("    nonSor: ").append(toIndentedString(nonSor)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    unitRate: ").append(toIndentedString(unitRate)).append("\n");
    sb.append("    estimateRate: ").append(toIndentedString(estimateRate)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    serviceTaxPerc: ").append(toIndentedString(serviceTaxPerc)).append("\n");
    sb.append("    revisionType: ").append(toIndentedString(revisionType)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
    sb.append("    estimateMeasurementSheets: ").append(toIndentedString(estimateMeasurementSheets)).append("\n");
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

