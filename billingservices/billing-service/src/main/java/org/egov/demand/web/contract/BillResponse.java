package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.model.BillInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * BillResponse
 */

public class BillResponse   {
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("BillInfos")
  private List<BillInfo> billInfos = new ArrayList<BillInfo>();
  
}

