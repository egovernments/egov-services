package org.egov.lcms.models;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds information about the advocate payment request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvocatePaymentRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("advocatePayments")
  private List<AdvocatePayment> advocatePayments = null;
}

