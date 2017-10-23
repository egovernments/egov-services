package org.egov.model;

import java.math.BigDecimal;

import org.egov.model.enums.TransactionType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disposal   {
	
  @JsonProperty("remarks")
  private String remarks;

  @JsonProperty("orderNumber")
  private String orderNumber;

  @JsonProperty("orderDate")
  private Long orderDate;

  @JsonProperty("tenantId")
  private String tenantId;

  @JsonProperty("id")
  private Long id;

  @JsonProperty("assetId")
  private Long assetId;

  @JsonProperty("buyerName")
  private String buyerName;

  @JsonProperty("buyerAddress")
  private String buyerAddress;

  @JsonProperty("disposalReason")
  private String disposalReason;

  @JsonProperty("disposalDate")
  private Long disposalDate;

  @JsonProperty("panCardNumber")
  private String panCardNumber;

  @JsonProperty("aadharCardNumber")
  private String aadharCardNumber;

  @JsonProperty("assetCurrentValue")
  private BigDecimal assetCurrentValue;

  @JsonProperty("saleValue")
  private BigDecimal saleValue;

  @JsonProperty("transactionType")
  private TransactionType transactionType;

  @JsonProperty("profitLossVoucherReference")
  private String profitLossVoucherReference;

  @JsonProperty("assetSaleAccount")
  private String assetSaleAccount;

  @JsonProperty("status")
  private String status;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails;

}

