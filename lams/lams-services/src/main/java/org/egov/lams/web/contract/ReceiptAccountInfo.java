package org.egov.lams.web.contract;

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
public class ReceiptAccountInfo {
  private String glCode = null;

  private String accountDescription = null;

  private String functionCode = null;

  private Double debitAmount = null;

  private Double creditAmount = null;

  private Integer orderNumber = null;

  private boolean isRevenueAccount;
  
  private String description;

  private String purpose;

}

