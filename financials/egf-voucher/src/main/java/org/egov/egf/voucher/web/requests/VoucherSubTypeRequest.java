package org.egov.egf.voucher.web.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.voucher.web.contract.VoucherSubTypeContract;

public @Data class VoucherSubTypeRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<VoucherSubTypeContract> voucherSubTypes = new ArrayList<VoucherSubTypeContract>();
}