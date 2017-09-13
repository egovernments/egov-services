package org.egov.egf.bill.web.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.BillPayeeDetailContract;

public @Data class BillPayeeDetailRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<BillPayeeDetailContract> billPayeeDetails = new ArrayList<BillPayeeDetailContract>();
}
