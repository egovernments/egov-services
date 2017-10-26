package org.egov.lcms.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the advocate payment response
 */
public class AdvocatePaymentResponse   {
  @JsonProperty("requestInfo")
  private ResponseInfo requestInfo = null;

  @JsonProperty("advocatepayments")
  private List<AdvocatePayment> advocatepayments = null;
}

