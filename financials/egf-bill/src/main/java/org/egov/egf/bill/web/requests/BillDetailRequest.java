package org.egov.egf.bill.web.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.BillDetailContract;

public @Data class BillDetailRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<BillDetailContract> billDetails = new ArrayList<BillDetailContract>();
}
