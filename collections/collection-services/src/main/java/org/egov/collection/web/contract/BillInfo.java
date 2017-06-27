package org.egov.collection.web.contract;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ToString
public class BillInfo   {
  private String payeeName;

  private String payeeAddress;

  private String payeeEmail;

  private String displayMessage;

  private String paidBy;

  private List<BillDetails> billDetails = new ArrayList<BillDetails>();

}

