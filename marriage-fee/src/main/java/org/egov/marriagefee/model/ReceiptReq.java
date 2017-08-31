package org.egov.marriagefee.model;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptReq   {
  @JsonProperty("tenantId")
  private String tenantId = null;
  
  @NotNull
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo;
  
  @NotNull
  @JsonProperty("Receipt")
  private List<Receipt> receipt = null;
  
  @JsonProperty("WorkflowDetails")
  private WorkflowDetailsRequest workflowDetails;


}
