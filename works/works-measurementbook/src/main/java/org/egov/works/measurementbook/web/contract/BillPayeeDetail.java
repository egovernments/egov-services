package org.egov.works.measurementbook.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * BillPayeeDetail
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class BillPayeeDetail   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("accountDetailType")
  private AccountDetailType accountDetailType = null;

  @JsonProperty("accountDetailKey")
  private AccountDetailKey accountDetailKey = null;

  @JsonProperty("amount")
  private Double amount = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public BillPayeeDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. 
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. ")

 @Size(min=0,max=256)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public BillPayeeDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the BillPayeeDetail 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the BillPayeeDetail ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BillPayeeDetail accountDetailType(AccountDetailType accountDetailType) {
    this.accountDetailType = accountDetailType;
    return this;
  }

   /**
   * account detail type of the BillPayeeDetail 
   * @return accountDetailType
  **/
  @ApiModelProperty(value = "account detail type of the BillPayeeDetail ")

  @Valid

  public AccountDetailType getAccountDetailType() {
    return accountDetailType;
  }

  public void setAccountDetailType(AccountDetailType accountDetailType) {
    this.accountDetailType = accountDetailType;
  }

  public BillPayeeDetail accountDetailKey(AccountDetailKey accountDetailKey) {
    this.accountDetailKey = accountDetailKey;
    return this;
  }

   /**
   * account detail key of the BillPayeeDetail 
   * @return accountDetailKey
  **/
  @ApiModelProperty(required = true, value = "account detail key of the BillPayeeDetail ")
  @NotNull

  @Valid

  public AccountDetailKey getAccountDetailKey() {
    return accountDetailKey;
  }

  public void setAccountDetailKey(AccountDetailKey accountDetailKey) {
    this.accountDetailKey = accountDetailKey;
  }

  public BillPayeeDetail amount(Double amount) {
    this.amount = amount;
    return this;
  }

   /**
   * amount of the BillPayeeDetail 
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "amount of the BillPayeeDetail ")
  @NotNull


  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public BillPayeeDetail auditDetails(AuditDetails auditDetails) {
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
    BillPayeeDetail billPayeeDetail = (BillPayeeDetail) o;
    return Objects.equals(this.tenantId, billPayeeDetail.tenantId) &&
        Objects.equals(this.id, billPayeeDetail.id) &&
        Objects.equals(this.accountDetailType, billPayeeDetail.accountDetailType) &&
        Objects.equals(this.accountDetailKey, billPayeeDetail.accountDetailKey) &&
        Objects.equals(this.amount, billPayeeDetail.amount) &&
        Objects.equals(this.auditDetails, billPayeeDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, id, accountDetailType, accountDetailKey, amount, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BillPayeeDetail {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    accountDetailType: ").append(toIndentedString(accountDetailType)).append("\n");
    sb.append("    accountDetailKey: ").append(toIndentedString(accountDetailKey)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

