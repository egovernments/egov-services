package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that holds the basic data of Detailed Estimate Deductions
 */
@ApiModel(description = "An Object that holds the basic data of Detailed Estimate Deductions")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class DetailedEstimateDeduction   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("chartOfAccounts")
  private ChartOfAccount chartOfAccounts = null;

  @JsonProperty("detailedEstimate")
  private String detailedEstimate = null;

  @JsonProperty("percentage")
  private Double percentage = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public DetailedEstimateDeduction id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Detailed Estimate Deduction
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Detailed Estimate Deduction")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DetailedEstimateDeduction tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Detailed Estimate Deduction
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Detailed Estimate Deduction")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public DetailedEstimateDeduction chartOfAccounts(ChartOfAccount chartOfAccounts) {
    this.chartOfAccounts = chartOfAccounts;
    return this;
  }

   /**
   * Chart Of Accounts of the Detailed Estimate Deduction from Financials
   * @return chartOfAccounts
  **/
  @ApiModelProperty(required = true, value = "Chart Of Accounts of the Detailed Estimate Deduction from Financials")
  @NotNull

  @Valid

  public ChartOfAccount getChartOfAccounts() {
    return chartOfAccounts;
  }

  public void setChartOfAccounts(ChartOfAccount chartOfAccounts) {
    this.chartOfAccounts = chartOfAccounts;
  }

  public DetailedEstimateDeduction detailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
    return this;
  }

   /**
   * Reference of the Detailed Estimate for Multi Year Estimate
   * @return detailedEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference of the Detailed Estimate for Multi Year Estimate")
  @NotNull


  public String getDetailedEstimate() {
    return detailedEstimate;
  }

  public void setDetailedEstimate(String detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
  }

  public DetailedEstimateDeduction percentage(Double percentage) {
    this.percentage = percentage;
    return this;
  }

   /**
   * Percentage for the Detailed Estimate Deduction
   * @return percentage
  **/
  @ApiModelProperty(value = "Percentage for the Detailed Estimate Deduction")


  public Double getPercentage() {
    return percentage;
  }

  public void setPercentage(Double percentage) {
    this.percentage = percentage;
  }

  public DetailedEstimateDeduction amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Amount for the Detailed Estimate Deduction
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "Amount for the Detailed Estimate Deduction")
  @NotNull

  @Valid

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public DetailedEstimateDeduction auditDetails(AuditDetails auditDetails) {
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
    DetailedEstimateDeduction detailedEstimateDeduction = (DetailedEstimateDeduction) o;
    return Objects.equals(this.id, detailedEstimateDeduction.id) &&
        Objects.equals(this.tenantId, detailedEstimateDeduction.tenantId) &&
        Objects.equals(this.chartOfAccounts, detailedEstimateDeduction.chartOfAccounts) &&
        Objects.equals(this.detailedEstimate, detailedEstimateDeduction.detailedEstimate) &&
        Objects.equals(this.percentage, detailedEstimateDeduction.percentage) &&
        Objects.equals(this.amount, detailedEstimateDeduction.amount) &&
        Objects.equals(this.auditDetails, detailedEstimateDeduction.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, chartOfAccounts, detailedEstimate, percentage, amount, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetailedEstimateDeduction {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    chartOfAccounts: ").append(toIndentedString(chartOfAccounts)).append("\n");
    sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
    sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

