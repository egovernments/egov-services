package org.egov.lcms.models;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the advocate payment request
 */

public class AdvocatePaymentRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("advocatepayments")
  private List<AdvocatePayment> advocatepayments = null;
}

