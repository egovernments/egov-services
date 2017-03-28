package org.egov.lams.web.contract;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiptAmountInfo {
  private Double arrearsAmount = null;

  private Double penaltyAmount = null;

  private Double currentInstallmentAmount = null;

  private Double advanceAmount = null;

  private Double latePaymentCharges = null;

  private Double arrearCess = null;

  private Double currentCess = null;

  private String installmentFrom = null;

  private String installmentTo = null;

  private Double reductionAmount = null;

  private String revenueWard = null;

  private Integer conflict = null;

  private String tenantId = null;

}

