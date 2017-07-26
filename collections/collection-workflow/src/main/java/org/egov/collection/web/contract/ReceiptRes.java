package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

@Setter
@Getter
@ToString
public class ReceiptRes   {
  
	private String tenantId;

  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo;
  
  @JsonProperty("Receipt")
  private List<Receipt> receipts;


}

