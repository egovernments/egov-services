package org.egov.bill.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * A detailed head wise dues which is payable on a Demand.
 */

public class BillInfo   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("demandId")
  private Long demandId = null;

  @JsonProperty("citizenName")
  private String citizenName = null;

  @JsonProperty("citizenAddress")
  private String citizenAddress = null;

  @JsonProperty("billNumber")
  private String billNumber = null;

  @JsonProperty("billType")
  private String billType = null;//Should be enum

  @JsonProperty("issuedDate")
  private Long issuedDate = null;

  @JsonProperty("lastDate")
  private Long lastDate = null;

  @JsonProperty("moduleName")
  private String moduleName = null;

  @JsonProperty("createdBy")
  private String createdBy = null;

  @JsonProperty("history")
  private String history = null;

  @JsonProperty("cancelled")
  private String cancelled = null;

  @JsonProperty("fundCode")
  private String fundCode = null;

  @JsonProperty("functionaryCode")
  private Long functionaryCode = null;

  @JsonProperty("fundSourceCode")
  private String fundSourceCode = null;

  @JsonProperty("departmentCode")
  private String departmentCode = null;

  @JsonProperty("collModesNotAllowed")
  private String collModesNotAllowed = null;

  @JsonProperty("boundaryNumber")
  private Integer boundaryNumber = null;

  @JsonProperty("boundaryType")
  private String boundaryType = null;

  @JsonProperty("billAmount")
  private Double billAmount = null;

  @JsonProperty("billAmountCollected")
  private Double billAmountCollected = null;

  @JsonProperty("serviceCode")
  private String serviceCode = null;

  @JsonProperty("partPaymentAllowed")
  private Boolean partPaymentAllowed = null;

  @JsonProperty("overrideAccHeadAllowed")
  private Boolean overrideAccHeadAllowed = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("minAmountPayable")
  private Double minAmountPayable = null;

  @JsonProperty("consumerCode")
  private String consumerCode = null;

  @JsonProperty("displayMessage")
  private String displayMessage = null;

  @JsonProperty("callbackForApportion")
  private Boolean callbackForApportion = null;

  @JsonProperty("emailId")
  private String emailId = null;

  @JsonProperty("consumerType")
  private String consumerType = null;

  @JsonProperty("totalAmount")
  private String totalAmount = null;

  @JsonProperty("billDetailInfos")
  private List<BillDetailInfo> billDetailInfos = new ArrayList<BillDetailInfo>();

}

