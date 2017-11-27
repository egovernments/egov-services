package org.egov.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
  @NotNull
  private String remarks;

  @JsonProperty("orderNumber")
  @NotNull
  private String orderNumber;

  @JsonProperty("orderDate")
  @NotNull
  private Long orderDate;

  @JsonProperty("tenantId")
  @NotNull
  private String tenantId;

  @JsonProperty("id")
  private Long id;

  @NotNull
  @JsonProperty("assetId")
  private Long assetId;

  @JsonProperty("buyerName")
  @NotNull
  private String buyerName;

  @JsonProperty("buyerAddress")
  private String buyerAddress;

  @JsonProperty("disposalReason")
  @NotNull
  private String disposalReason;

  @JsonProperty("disposalDate")
  @NotNull
  private Long disposalDate;

  @JsonProperty("panCardNumber")
  private String panCardNumber;

  @JsonProperty("aadharCardNumber")
  private String aadharCardNumber;

  @JsonProperty("assetCurrentValue")
  private BigDecimal assetCurrentValue;

  @JsonProperty("saleValue")
  @NotNull
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

