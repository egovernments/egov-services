package org.egov.tradelicense.notification.web.responses;

import java.util.List;

import org.egov.tradelicense.notification.web.contract.PaginationContract;
import org.egov.tradelicense.notification.web.contract.Receipt;

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
  private CollectionResponseInfo responseInfo;
  
  @JsonProperty("Receipt")
  private List<Receipt> receipts;

  private PaginationContract page;
  
}
