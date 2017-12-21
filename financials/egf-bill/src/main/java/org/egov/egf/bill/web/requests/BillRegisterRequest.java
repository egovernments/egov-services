package org.egov.egf.bill.web.requests;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.domain.model.BillRegister;

import lombok.Data;

public @Data class BillRegisterRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<BillRegister> billRegisters = new ArrayList<>();
}