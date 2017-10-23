package org.egov.model;

import java.math.BigDecimal;

import org.egov.model.enums.TypeOfChange;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revaluation   {
	
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

  @JsonProperty("currentCapitalizedValue")
  private BigDecimal currentCapitalizedValue;

  @JsonProperty("typeOfChange")
  private TypeOfChange typeOfChange;

  @JsonProperty("revaluationAmount")
  private BigDecimal revaluationAmount;

  @JsonProperty("valueAfterRevaluation")
  private BigDecimal valueAfterRevaluation;

  @JsonProperty("revaluationDate")
  private Long revaluationDate;

  @JsonProperty("revaluatedBy")
  private String revaluatedBy;

  @JsonProperty("reasonForRevaluation")
  private String reasonForRevaluation;

  @JsonProperty("voucherReference")
  private String voucherReference;

  @JsonProperty("fixedAssetsWrittenOffAccount")
  private String fixedAssetsWrittenOffAccount;

  @JsonProperty("function")
  private String function;

  @JsonProperty("fund")
  private String fund;

  @JsonProperty("scheme")
  private String scheme;

  @JsonProperty("subScheme")
  private String subScheme;

  @JsonProperty("comments")
  private String comments;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails;

  @JsonProperty("status")
  private String status;

}

