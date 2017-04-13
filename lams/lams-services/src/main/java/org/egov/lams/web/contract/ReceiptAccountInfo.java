package org.egov.lams.web.contract;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ReceiptAccountInfo {
  private String glCode ;

  private String function ;
  
  private String functionName;

  private Double drAmount ;

  private Double crAmount ;

  private Long orderNumber ;

  private boolean isRevenueAccount; 
  
  private String description;

  private String purpose;

  private String accountName;
  
  private String financialYear;

  private Double creditAmountToBePaid;
}

