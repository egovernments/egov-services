package org.egov.tradelicense.notification.web.contract;

import java.util.List;

import org.egov.tl.commons.web.contract.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReceiptRes   {
  
  private String tenantId;

  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo;
  
  @JsonProperty("Receipt")
  private List<Receipt> receipts;

  private PaginationContract page;
  
}
