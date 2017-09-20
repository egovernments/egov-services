package org.egov.egf.voucher.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.voucher.web.contract.VoucherContract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class VoucherResponse {
    private ResponseInfo responseInfo;
    private List<VoucherContract> vouchers;
    private PaginationContract page;
}