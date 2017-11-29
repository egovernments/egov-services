package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that holds Activities for a given Estimate template. Either SOR or NON SOR is mandatory.
 */
@ApiModel(description = "An Object that holds Activities for a given Estimate template. Either SOR or NON SOR is mandatory.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:02:10.583Z")

public class EstimateTemplateActivities   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("estimateTemplate")
  private String estimateTemplate = null;

  @JsonProperty("scheduleOfRate")
  private String scheduleOfRate = null;

  @JsonProperty("uom")
  private String uom = null;

  @JsonProperty("nonSOR")
  private NonSOR nonSOR = null;

  @JsonProperty("unitRate")
  private BigDecimal unitRate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public EstimateTemplateActivities id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Estimate Template Activitie
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Estimate Template Activitie")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstimateTemplateActivities tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Estimate Template Activitie
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Estimate Template Activitie")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstimateTemplateActivities estimateTemplate(String estimateTemplate) {
    this.estimateTemplate = estimateTemplate;
    return this;
  }

   /**
   * The estimate template of the Template activity. Unique reference from 'EstimateTemplate'. Primary key is ref. here.
   * @return estimateTemplate
  **/
  @ApiModelProperty(value = "The estimate template of the Template activity. Unique reference from 'EstimateTemplate'. Primary key is ref. here.")


  public String getEstimateTemplate() {
    return estimateTemplate;
  }

  public void setEstimateTemplate(String estimateTemplate) {
    this.estimateTemplate = estimateTemplate;
  }

  public EstimateTemplateActivities scheduleOfRate(String scheduleOfRate) {
    this.scheduleOfRate = scheduleOfRate;
    return this;
  }

   /**
   * The Schedue of Rate of the Template activity. Unique reference from 'ScheduleOfRate'. Primary key is ref. here.
   * @return scheduleOfRate
  **/
  @ApiModelProperty(value = "The Schedue of Rate of the Template activity. Unique reference from 'ScheduleOfRate'. Primary key is ref. here.")


  public String getScheduleOfRate() {
    return scheduleOfRate;
  }

  public void setScheduleOfRate(String scheduleOfRate) {
    this.scheduleOfRate = scheduleOfRate;
  }

  public EstimateTemplateActivities uom(String uom) {
    this.uom = uom;
    return this;
  }

   /**
   * UOM for the Estimate Template Activity. Unique reference from 'UOM'. Code is ref. here.
   * @return uom
  **/
  @ApiModelProperty(required = true, value = "UOM for the Estimate Template Activity. Unique reference from 'UOM'. Code is ref. here.")
  @NotNull


  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }

  public EstimateTemplateActivities nonSOR(NonSOR nonSOR) {
    this.nonSOR = nonSOR;
    return this;
  }

   /**
   * The Non SOR of the Template activity
   * @return nonSOR
  **/
  @ApiModelProperty(value = "The Non SOR of the Template activity")

  @Valid

  public NonSOR getNonSOR() {
    return nonSOR;
  }

  public void setNonSOR(NonSOR nonSOR) {
    this.nonSOR = nonSOR;
  }

  public EstimateTemplateActivities unitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
    return this;
  }

   /**
   * Unit Rate for Estimate Template Activitie
   * @return unitRate
  **/
  @ApiModelProperty(value = "Unit Rate for Estimate Template Activitie")

  @Valid

  public BigDecimal getUnitRate() {
    return unitRate;
  }

  public void setUnitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
  }

  public EstimateTemplateActivities auditDetails(AuditDetails auditDetails) {
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
    EstimateTemplateActivities estimateTemplateActivities = (EstimateTemplateActivities) o;
    return Objects.equals(this.id, estimateTemplateActivities.id) &&
        Objects.equals(this.tenantId, estimateTemplateActivities.tenantId) &&
        Objects.equals(this.estimateTemplate, estimateTemplateActivities.estimateTemplate) &&
        Objects.equals(this.scheduleOfRate, estimateTemplateActivities.scheduleOfRate) &&
        Objects.equals(this.uom, estimateTemplateActivities.uom) &&
        Objects.equals(this.nonSOR, estimateTemplateActivities.nonSOR) &&
        Objects.equals(this.unitRate, estimateTemplateActivities.unitRate) &&
        Objects.equals(this.auditDetails, estimateTemplateActivities.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, estimateTemplate, scheduleOfRate, uom, nonSOR, unitRate, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateTemplateActivities {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    estimateTemplate: ").append(toIndentedString(estimateTemplate)).append("\n");
    sb.append("    scheduleOfRate: ").append(toIndentedString(scheduleOfRate)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    nonSOR: ").append(toIndentedString(nonSOR)).append("\n");
    sb.append("    unitRate: ").append(toIndentedString(unitRate)).append("\n");
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

