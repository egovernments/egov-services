package org.egov.collection.web.contract;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class BillAccountDetails   {
  private String glcode;

  private Integer order;

  private String accountDescription;

  private Double creditAmount;

  private Double debitAmount;

  private String functionCode;

  private Boolean isActualDemand;
}

