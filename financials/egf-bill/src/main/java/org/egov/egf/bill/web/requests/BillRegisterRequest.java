package org.egov.egf.bill.web.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.BillRegisterContract;

public @Data class BillRegisterRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<BillRegisterContract> billRegisters = new ArrayList<BillRegisterContract>();
}