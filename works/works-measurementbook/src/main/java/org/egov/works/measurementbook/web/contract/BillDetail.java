package org.egov.works.measurementbook.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * BillDetail
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T09:56:01.690Z")

public class BillDetail   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("orderId")
  private Integer orderId = null;

  @JsonProperty("chartOfAccount")
  private ChartOfAccount chartOfAccount = null;

  @JsonProperty("debitAmount")
  private Double debitAmount = null;

  @JsonProperty("creditAmount")
  private Double creditAmount = null;

  @JsonProperty("function")
  private Function function = null;

  @JsonProperty("billPayeeDetails")
  private List<BillPayeeDetail> billPayeeDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public BillDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. 
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. ")
  @NotNull

 @Size(min=0,max=256)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public BillDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the BillDetail 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the BillDetail ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BillDetail orderId(Integer orderId) {
    this.orderId = orderId;
    return this;
  }

   /**
   * order id of the BillDetail 
   * @return orderId
  **/
  @ApiModelProperty(value = "order id of the BillDetail ")


  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public BillDetail chartOfAccount(ChartOfAccount chartOfAccount) {
    this.chartOfAccount = chartOfAccount;
    return this;
  }

   /**
   * chart of account of the BillDetail 
   * @return chartOfAccount
  **/
  @ApiModelProperty(required = true, value = "chart of account of the BillDetail ")
  @NotNull

  @Valid

  public ChartOfAccount getChartOfAccount() {
    return chartOfAccount;
  }

  public void setChartOfAccount(ChartOfAccount chartOfAccount) {
    this.chartOfAccount = chartOfAccount;
  }

  public BillDetail debitAmount(Double debitAmount) {
    this.debitAmount = debitAmount;
    return this;
  }

   /**
   * debit amount of the BillDetail 
   * @return debitAmount
  **/
  @ApiModelProperty(required = true, value = "debit amount of the BillDetail ")
  @NotNull


  public Double getDebitAmount() {
    return debitAmount;
  }

  public void setDebitAmount(Double debitAmount) {
    this.debitAmount = debitAmount;
  }

  public BillDetail creditAmount(Double creditAmount) {
    this.creditAmount = creditAmount;
    return this;
  }

   /**
   * credit amount of the BillDetail 
   * @return creditAmount
  **/
  @ApiModelProperty(required = true, value = "credit amount of the BillDetail ")
  @NotNull


  public Double getCreditAmount() {
    return creditAmount;
  }

  public void setCreditAmount(Double creditAmount) {
    this.creditAmount = creditAmount;
  }

  public BillDetail function(Function function) {
    this.function = function;
    return this;
  }

   /**
   * function of the BillDetail 
   * @return function
  **/
  @ApiModelProperty(value = "function of the BillDetail ")

  @Valid

  public Function getFunction() {
    return function;
  }

  public void setFunction(Function function) {
    this.function = function;
  }

  public BillDetail billPayeeDetails(List<BillPayeeDetail> billPayeeDetails) {
    this.billPayeeDetails = billPayeeDetails;
    return this;
  }

  public BillDetail addBillPayeeDetailsItem(BillPayeeDetail billPayeeDetailsItem) {
    if (this.billPayeeDetails == null) {
      this.billPayeeDetails = new ArrayList<BillPayeeDetail>();
    }
    this.billPayeeDetails.add(billPayeeDetailsItem);
    return this;
  }

   /**
   * bill payee details of the BillDetail 
   * @return billPayeeDetails
  **/
  @ApiModelProperty(value = "bill payee details of the BillDetail ")

  @Valid

  public List<BillPayeeDetail> getBillPayeeDetails() {
    return billPayeeDetails;
  }

  public void setBillPayeeDetails(List<BillPayeeDetail> billPayeeDetails) {
    this.billPayeeDetails = billPayeeDetails;
  }

  public BillDetail auditDetails(AuditDetails auditDetails) {
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
    BillDetail billDetail = (BillDetail) o;
    return Objects.equals(this.tenantId, billDetail.tenantId) &&
        Objects.equals(this.id, billDetail.id) &&
        Objects.equals(this.orderId, billDetail.orderId) &&
        Objects.equals(this.chartOfAccount, billDetail.chartOfAccount) &&
        Objects.equals(this.debitAmount, billDetail.debitAmount) &&
        Objects.equals(this.creditAmount, billDetail.creditAmount) &&
        Objects.equals(this.function, billDetail.function) &&
        Objects.equals(this.billPayeeDetails, billDetail.billPayeeDetails) &&
        Objects.equals(this.auditDetails, billDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, id, orderId, chartOfAccount, debitAmount, creditAmount, function, billPayeeDetails, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BillDetail {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    chartOfAccount: ").append(toIndentedString(chartOfAccount)).append("\n");
    sb.append("    debitAmount: ").append(toIndentedString(debitAmount)).append("\n");
    sb.append("    creditAmount: ").append(toIndentedString(creditAmount)).append("\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
    sb.append("    billPayeeDetails: ").append(toIndentedString(billPayeeDetails)).append("\n");
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

