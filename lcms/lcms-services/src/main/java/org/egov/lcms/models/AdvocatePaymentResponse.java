package org.egov.lcms.models;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the advocate payment response
 */
public class AdvocatePaymentResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("advocatepayments")
  private List<AdvocatePayment> advocatepayments = null;
}

