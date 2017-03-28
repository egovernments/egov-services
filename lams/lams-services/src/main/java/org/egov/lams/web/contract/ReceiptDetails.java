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
public class ReceiptDetails {
  private Long receiptHeader = null;

  private Long accounthead = null;

  private Double cramountToBePaid = null;

  private Double cramount = null;

  private Integer orderNumber = null;

  private String accountDescription = null;

  private String function = null;

  private Boolean isActualDemand = null;

  private Integer financialYear = null;

}

