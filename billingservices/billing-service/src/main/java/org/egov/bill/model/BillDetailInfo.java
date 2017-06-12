package org.egov.bill.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Holds the head wise break up of the dues.
 */

public class BillDetailInfo   {
  @JsonProperty("billId")
  private Long billId = null;

  @JsonProperty("creditAmount")
  private Double creditAmount = null;

  @JsonProperty("debitAmount")
  private Double debitAmount = null;

  @JsonProperty("amountCollected")
  private Double amountCollected = null;

  @JsonProperty("glCode")
  private String glCode = null;

  @JsonProperty("functionCode")
  private String functionCode = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("purpose")
  private String purpose = null;

  @JsonProperty("period")
  private String period = null;

  @JsonProperty("orderNo")
  private Integer orderNo = null;

  @JsonProperty("isActualDemand")
  private BigDecimal isActualDemand = null;

}

