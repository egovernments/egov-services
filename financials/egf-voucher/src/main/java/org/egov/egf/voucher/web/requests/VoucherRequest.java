package org.egov.egf.voucher.web.requests;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.voucher.web.contract.VoucherContract;

import lombok.Data;

public @Data class VoucherRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<VoucherContract> vouchers = new ArrayList<VoucherContract>();
}