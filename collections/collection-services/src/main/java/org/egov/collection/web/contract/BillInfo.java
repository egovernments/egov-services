package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ToString
public class BillInfo   {
  private Object payeeName;

  private String payeeAddress;

  @JsonProperty("payeeEmail")
  private String payeeEmail = null;

  @JsonProperty("displayMessage")
  private String displayMessage = null;

  @JsonProperty("paidBy")
  private String paidBy = null;

  @JsonProperty("billDetails")
  private List<BillDetails> billDetails = new ArrayList<BillDetails>();

}

