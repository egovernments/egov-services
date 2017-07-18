package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReceiptReq   {
  @JsonProperty("tenantId")
  private String tenantId = null;
  
  @NotNull
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo;
  
  @NotNull
  @JsonProperty("Receipt")
  private Receipt receipt = null;
   
}

