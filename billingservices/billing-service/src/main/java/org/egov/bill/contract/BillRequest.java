package org.egov.bill.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.bill.model.BillInfo;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * BillRequest
 */
public class BillRequest   {
	
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("BillInfos")
  private List<BillInfo> billInfos = new ArrayList<BillInfo>();
}

