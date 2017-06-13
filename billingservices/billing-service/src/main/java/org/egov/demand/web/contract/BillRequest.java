package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.BillInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * BillRequest
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequest   {
	
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("BillInfos")
  private List<BillInfo> billInfos = new ArrayList<BillInfo>();
}

