package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo;
  
  @JsonProperty("Receipt")
  private Receipt receipt = null;
   
}

