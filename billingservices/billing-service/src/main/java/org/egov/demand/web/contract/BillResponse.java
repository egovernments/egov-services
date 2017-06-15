package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.model.Bill;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * BillResponse
 */

public class BillResponse   {
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("BillInfos")
  private List<Bill> billInfos = new ArrayList<Bill>();
  
}

