package org.egov.demand.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailInfo   {
	
  @JsonProperty("billId")
  private Long billId;

  @JsonProperty("creditAmount")
  private Double creditAmount;

  @JsonProperty("debitAmount")
  private Double debitAmount;

  @JsonProperty("amountCollected")
  private Double amountCollected;

  @JsonProperty("glCode")
  private String glCode;

  @JsonProperty("functionCode")
  private String functionCode;

  @JsonProperty("description")
  private String description;

  @JsonProperty("purpose")
  private String purpose;

  @JsonProperty("period")
  private String period;

  @JsonProperty("orderNo")
  private Integer orderNo;

  @JsonProperty("isActualDemand")
  private BigDecimal isActualDemand;
}

